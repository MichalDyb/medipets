package kpu.krosno.MediPets.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import kpu.krosno.MediPets.models.MedsPrescriptions;
import kpu.krosno.MediPets.models.Pets;
import kpu.krosno.MediPets.models.Prescriptions;
import kpu.krosno.MediPets.models.Visits;
import kpu.krosno.MediPets.repositories.AccountsRepository;
import kpu.krosno.MediPets.repositories.PetsRepository;
import kpu.krosno.MediPets.repositories.PrescriptionsRepository;
import kpu.krosno.MediPets.repositories.VisitsRepository;
import kpu.krosno.MediPets.security.AccountDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Controller
public class ClientController {
    @Autowired
    private PetsRepository petsRepository;
    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private PrescriptionsRepository prescriptionsRepository;
    @Autowired
    private VisitsRepository visitsRepository;

    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public String getIndex(Model model)
    {
        return "/client/index";
    }

    // GET: Pets
    @RequestMapping(value = "/client/pets", method = RequestMethod.GET)
    public String getPets(Model model)
    {
        AccountDetails user = (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("pets", petsRepository.findAllByPersonId(accountsRepository.findAllByEmailAndIsEnabled(user.getUsername(), true).get(0).getPersonId()));
        return "/client/pets/index";
    }

    // GET: Pet details
    @RequestMapping(value = "/client/pets/{id}", method = RequestMethod.GET)
    public String getPetDetails(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        try {
            Pets pet = petsRepository.findByPetId(Long.parseLong(id));
            if(pet == null) throw new NumberFormatException("Brak");
            model.addAttribute("pet", pet);
        }
        catch (NumberFormatException exception)
        {
            return "redirect:/client/pets";
        }
        return "/client/pets/details";
    }

    // GET: Prescriptions
    @RequestMapping(value = "/client/prescriptions", method = RequestMethod.GET)
    public String getPrescriptions(Model model)
    {
        AccountDetails user = (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("prescriptions", prescriptionsRepository.findAllByClientId(accountsRepository.findAllByEmailAndIsEnabled(user.getUsername(), true).get(0).getPersonId()));
        return "/client/prescriptions/index";
    }

    // GET: Prescription details
    @RequestMapping(value = "/client/prescriptions/{id}", method = RequestMethod.GET)
    public String getPrescriptionDetails(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        try {
            model.addAttribute("prescription", prescriptionsRepository.findByPrescriptionId(Long.parseLong(id)));
        }
        catch (NumberFormatException | EntityNotFoundException exception)
        {
            return "redirect:/client/prescriptions";
        }
        return "/client/prescriptions/details";
    }
    // GET: Prescription PDF
    @RequestMapping(value = "/client/prescriptions/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getPrescriptionPDF(@PathVariable(name = "id", value = "id") String id) throws DocumentException, IOException {
        Prescriptions prescription = null;
        try
        {
            prescription = prescriptionsRepository.findByPrescriptionId(Long.parseLong(id));
            if (prescription == null)
                throw new EntityNotFoundException();
        }
        catch (NumberFormatException exception)
        {
            return ResponseEntity.badRequest().build();
        }
        catch (EntityNotFoundException exception)
        {
            return ResponseEntity.notFound().build();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();
        document.addTitle("Recepta");
        document.addAuthor(prescription.getDoctorId().getName() + " " + prescription.getDoctorId().getSurname());
        BaseFont baseFont = BaseFont.createFont("arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 14);
        Paragraph paragraph = new Paragraph("Data wystawienia: " + prescription.getPrescriptionDate().toString(), font);
        document.add(paragraph);
        paragraph = new Paragraph("Wystawiona przez: " + prescription.getDoctorId().getName() + " " + prescription.getDoctorId().getSurname(), font);
        document.add(paragraph);
        paragraph = new Paragraph("Dla: " + prescription.getClientId().getName() + " " + prescription.getClientId().getSurname(), font);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);
        if (!prescription.getMedsPrescriptions().isEmpty())
        {
            PdfPTable table = new PdfPTable(3);
            Stream.of("Nazwa leku", "Ilość", "Opis").forEach(title -> {
                PdfPCell cell = new PdfPCell();
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setBorderWidth(2);
                cell.setPhrase(new Phrase(title, font));
                table.addCell(cell);
            });
            for (MedsPrescriptions medPrescription: prescription.getMedsPrescriptions()) {
                table.addCell(new Phrase(medPrescription.getMedicineId().getMedicine(), font));
                table.addCell(new Phrase(medPrescription.getMedQuantity().toString(), font));
                table.addCell(new Phrase(medPrescription.getPrescriptionDescription(), font));
            }
            document.add(table);
        }
        else
        {
            paragraph = new Paragraph("Brak leków.", font);
            document.add(paragraph);
        }
        document.close();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(byteArrayOutputStream.toByteArray());
    }

    // GET: Visits
    @RequestMapping(value = "/client/visits", method = RequestMethod.GET)
    public String getVisits(Model model)
    {
        AccountDetails user = (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Pets> pets = petsRepository.findAllByPersonId(accountsRepository.findAllByEmailAndIsEnabled(user.getUsername(), true).get(0).getPersonId());
        model.addAttribute("visits", visitsRepository.findAllByPetIdIn(pets));
        return "/client/visits/index";
    }

    // GET: Cancel visit
    @RequestMapping(value = "/client/visits/cancelVisit/{id}", method = RequestMethod.GET)
    public String getCancelVisit(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        Visits visit = null;
        try
        {
            visit = visitsRepository.findByVisitId(Long.parseLong(id));
            if (visit == null)
                throw new EntityNotFoundException();
        }
        catch (EntityNotFoundException | NumberFormatException exception)
        {
            return "redirect:/client/visits";
        }
        model.addAttribute("visit", visit);
        return "/client/visits/cancel";
    }

    // GET: Cancel visit
    @RequestMapping(value = "/client/visits/confirmCancelVisit/{id}", method = RequestMethod.GET)
    public String getConfirmCancelVisit(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        Visits visit = null;
        try
        {
            visit = visitsRepository.findByVisitId(Long.parseLong(id));
            if (visit == null)
                throw new EntityNotFoundException();
        }
        catch (EntityNotFoundException | NumberFormatException exception)
        {
            return "redirect:/client/visits";
        }
        visit.setVisitStatus("Anulowano");
        visitsRepository.save(visit);
        return "redirect:/client/visits";
    }

    // GET: Visit details
    @RequestMapping(value = "/client/visits/{id}", method = RequestMethod.GET)
    public String getVisitDetails(@PathVariable(name = "id", value = "id") String id, Model model)
    {
        Visits visit = null;
        try
        {
            visit = visitsRepository.findByVisitId(Long.parseLong(id));
            if (visit == null)
                throw new EntityNotFoundException();
        }
        catch (EntityNotFoundException | NumberFormatException exception)
        {
            return "/client/visits";
        }
        model.addAttribute("visit", visit);
        return "/client/visits/details";
    }
}
