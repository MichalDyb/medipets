package kpu.krosno.MediPets.utility;

import org.springframework.ui.Model;

public abstract class CustomValidator {
    public static boolean emailValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("emailError", "Podaj email.");
            return true;
        }
        if(value.length() > 255)
        {
            model.addAttribute("emailError", "Email może zawierać co najwyżej 255 znaków.");
            return true;
        }
        if(!value.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"))
        {
            model.addAttribute("emailError", "Email jest nieprawidłowy.");
            return true;
        }
        return false;
    }

    public static boolean passwordValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("passwordError", "Podaj hasło.");
            return true;
        }
        if(value.length() < 8 || value.length() > 20)
        {
            model.addAttribute("passwordError", "Hasło musi zawierać od 8 do 20 znaków.");
            return true;
        }
        if(!value.matches("^(?=.*[0-9])(?=.*[a-ząćęłńóśźż])(?=.*[A-ZĄĆĘŁŃÓŚŹŻ])(?=.*[!@#$%*^&+=])(?=\\S+$).{8,}$"))
        {
            model.addAttribute("passwordError", "Hasło musi zawierać co najmniej jedną cyfre, małą litere, dużą literę i znak specjalny.");
            return true;
        }
        return false;
    }

    public static boolean passwordConfirmValidation(Model model, String passwordConfirm, String password)
    {
        if(passwordConfirm == null || passwordConfirm.isEmpty()) {
            model.addAttribute("passwordConfirmError", "Ponów hasło.");
            return true;
        }
        if(!passwordConfirm.equals(password)) {
            model.addAttribute("passwordConfirmError", "Podane hasła są różne.");
            return true;
        }
        return false;
    }

    public static boolean nameValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("nameError", "Podaj imię.");
            return true;
        }
        if(value.length() > 30)
        {
            model.addAttribute("nameError", "Imię może zawierać co najwyżej 30 znaków.");
            return true;
        }
        if(!value.matches("^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*$"))
        {
            model.addAttribute("nameError", "Imię może składać się tylko z liter (pierwsza litera musi być wielka).");
            return true;
        }
        return false;
    }

    public static boolean surnameValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("surnameError", "Podaj nazwisko.");
            return true;
        }
        if(value.length() > 30)
        {
            model.addAttribute("surnameError", "Nazwisko może zawierać co najwyżej 30 znaków.");
            return true;
        }
        if(!value.matches("^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*$"))
        {
            model.addAttribute("surnameError", "Nazwisko może składać się tylko z liter (pierwsza litera musi być wielka).");
            return true;
        }
        return false;
    }

    public static boolean peselValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("peselError", "Podaj pesel.");
            return true;
        }
        if(!value.matches("^[0-9]+$") || value.length() != 11)
        {
            model.addAttribute("peselError", "Pesel składa sie z 11 cyfr.");
            return true;
        }
        return false;
    }

    public static boolean phoneNumberValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("phoneNumberError", "Podaj numer telefonu.");
            return true;
        }
        if(!value.matches("^[0-9]+$") || value.length() != 9)
        {
            model.addAttribute("phoneNumberError", "Numer telefonu składa sie z 9 cyfr.");
            return true;
        }
        return false;
    }

    public static boolean postcodeValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("postcodeError", "Podaj kod pocztowy.");
            return true;
        }
        if(!value.matches("^[0-9]+-[0-9]+$") || value.length() != 6)
        {
            model.addAttribute("postcodeError", "Podaj kod pocztowy w formacie XX-XXX");
            return true;
        }
        return false;
    }

    public static boolean cityValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("cityError", "Podaj miejscowość.");
            return true;
        }
        if(value.length() > 50)
        {
            model.addAttribute("cityError", "Miejscowość może zawierać co najwyżej 50 znaków.");
            return true;
        }
        if(!value.matches("^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż\\s]*$"))
        {
            model.addAttribute("cityError", "Miejscowość może składać się tylko z liter lub spacji (pierwsza litera musi być wielka).");
            return true;
        }
        return false;
    }

    public static boolean streetValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("streetError", "Podaj ulicę.");
            return true;
        }
        if(value.length() > 75)
        {
            model.addAttribute("streetError", "Ulica może zawierać co najwyżej 75 znaków.");
            return true;
        }
        if(!value.matches("^[0-9A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż][0-9A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż\\-\\s]*$"))
        {
            model.addAttribute("streetError", "Ulica może składać się tylko z liter, cyfr, spacji i myślnika.");
            return true;
        }
        return false;
    }

    public static boolean petNameValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("petNameError", "Podaj imię zwierzaka.");
            return true;
        }
        if(value.length() > 30)
        {
            model.addAttribute("petNameError", "Imię zwierzaka może zawierać co najwyżej 30 znaków.");
            return true;
        }
        if(!value.matches("^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*$"))
        {
            model.addAttribute("petNameError", "Imię zwierzaka może składać się tylko z liter (pierwsza litera musi być wielka).");
            return true;
        }
        return false;
    }

    public static boolean dateOfBirthValidation(Model model, String value) {
        if(value == null || value.isEmpty()) {
            model.addAttribute("dateOfBirthError", "Podaj datę urodzenia zwierzaka.");
            return true;
        }
        return false;
    }

    public static boolean genderValidation(Model model, String value) {
        if(value == null || value.isEmpty()) {
            model.addAttribute("genderError", "Podaj płeć zwierzaka.");
            return true;
        }
        if(value.length() > 20)
        {
            model.addAttribute("genderError", "Płeć może zawierać co najwyżej 20 znaków.");
            return true;
        }
        return false;
    }

    public static boolean petDescriptionValidation(Model model, String value) {
        if(value.length() > 80)
        {
            model.addAttribute("petDescriptionError", "Opis zwierzaka może zawierać co najwyżej 80 znaków.");
            return true;
        }
        return false;
    }

    public static boolean speciesValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("speciesError", "Podaj nazwę gatunku.");
            return true;
        }
        if(value.length() > 30)
        {
            model.addAttribute("speciesError", "Nazwa gatunku może zawierać co najwyżej 30 znaków.");
            return true;
        }
        if(!value.matches("^[A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż][A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż\\-\\s]*$"))
        {
            model.addAttribute("speciesError", "Nazwa gatunku może składać się tylko z liter lub spacji.");
            return true;
        }
        return false;
    }

    public static boolean breedValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("breedError", "Podaj nazwę rasy.");
            return true;
        }
        if(value.length() > 30)
        {
            model.addAttribute("breedError", "Nazwa rasy może zawierać co najwyżej 30 znaków.");
            return true;
        }
        if(!value.matches("^[A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż][A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż\\-\\s]*$"))
        {
            model.addAttribute("breedError", "Nazwa rasy może składać się tylko z liter lub spacji.");
            return true;
        }
        return false;
    }

    public static boolean medicineValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("medicineError", "Podaj nazwę leku.");
            return true;
        }
        if(value.length() > 60)
        {
            model.addAttribute("medicineError", "Nazwa leku może zawierać co najwyżej 60 znaków.");
            return true;
        }
        if(!value.matches("^[0-9A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż][0-9A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż\\-\\s]*$"))
        {
            model.addAttribute("medicineError", "Nazwa leku może składać się tylko z liter, cyfr, spacji i myślnika.");
            return true;
        }
        return false;
    }

    public static boolean medicinePriceValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("medicinePriceError", "Podaj cenę leku.");
            return true;
        }
        if(value.matches("^0$") || value.matches("^0\\.0$") || value.matches("^0\\.00$"))
        {
            model.addAttribute("medicinePriceError", "Cena leku musi być większa od 0.");
            return true;
        }
        if(!value.matches("^0\\.[0-9]?[0-9]$") && !value.matches("^[1-9]+[0-9]*\\.[0-9]?[0-9]$") && !value.matches("^[1-9]+[0-9]*$") && !value.matches("^0$"))
        {
            model.addAttribute("medicinePriceError", "Podaj cenę leku jako liczbę zmiennoprzecinkową o maksymalnej precyzji 2.");
            return true;
        }
        return false;
    }

    public static boolean prescriptionDateValidation(Model model, String value) {
        if(value == null || value.isEmpty()) {
            model.addAttribute("prescriptionDateError", "Podaj datę wystawienia recepty.");
            return true;
        }
        return false;
    }

    public static boolean medQuantityValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("medQuantityError", "Podaj ilość opakowań leku.");
            return true;
        }
        if(value.matches("^0$") || value.matches("^0\\.0$") || value.matches("^0\\.00$"))
        {
            model.addAttribute("medQuantityError", "Ilość opakowań leku musi być większa od 0.");
            return true;
        }
        if(!value.matches("^[1-9]+[0-9]*$"))
        {
            model.addAttribute("medQuantityError", "Podaj ilość opakowań leku jako liczbe całkowitą dodatnią.");
            return true;
        }
        return false;
    }

    public static boolean prescriptionDescriptionValidation(Model model, String value) {
        if(value == null || value.isEmpty()) {
            model.addAttribute("prescriptionDescriptionError", "Podaj opis recepty.");
            return true;
        }
        if(value.length() > 80)
        {
            model.addAttribute("prescriptionDescriptionError", "Opis recepty może zawierać co najwyżej 80 znaków.");
            return true;
        }
        return false;
    }

    public static boolean treatmentValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("treatmentError", "Podaj nazwę zabiegu.");
            return true;
        }
        if(value.length() > 60)
        {
            model.addAttribute("treatmentError", "Nazwa zabiegu może zawierać co najwyżej 60 znaków.");
            return true;
        }
        if(!value.matches("^[0-9A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż][0-9A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż\\-\\s]*$"))
        {
            model.addAttribute("treatmentError", "Nazwa zabiegu może składać się tylko z liter, cyfr, spacji i myślnika.");
            return true;
        }
        return false;
    }

    public static boolean treatmentPriceValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("treatmentPriceError", "Podaj cenę zabiegu.");
            return true;
        }
        if(value.matches("^0$") || value.matches("^0\\.0$") || value.matches("^0\\.00$"))
        {
            model.addAttribute("treatmentPriceError", "Cena zabiegu musi być większa od 0.");
            return true;
        }
        if(!value.matches("^0\\.[0-9]?[0-9]$") && !value.matches("^[1-9]+[0-9]*\\.[0-9]?[0-9]$") && !value.matches("^[1-9]+[0-9]*$") && !value.matches("^0$"))
        {
            model.addAttribute("treatmentPriceError", "Podaj cenę zabiegu jako liczbę zmiennoprzecinkową o maksymalnej precyzji 2.");
            return true;
        }
        return false;
    }

    public static boolean visitDateValidation(Model model, String value) {
        if(value == null || value.isEmpty()) {
            model.addAttribute("visitDateError", "Podaj datę wizyty.");
            return true;
        }
        return false;
    }

    public static boolean visitTimeValidation(Model model, String value) {
        if(value == null || value.isEmpty()) {
            model.addAttribute("visitTimeError", "Podaj godzinę wizyty.");
            return true;
        }
        return false;
    }

    public static boolean visitPriceValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("visitPriceError", "Podaj cenę wizyty.");
            return true;
        }
        if(!value.matches("^0\\.[0-9]?[0-9]$") && !value.matches("^[1-9]+[0-9]*\\.[0-9]?[0-9]$") && !value.matches("^[1-9]+[0-9]*$") && !value.matches("^0$"))
        {
            model.addAttribute("visitPriceError", "Podaj cenę wizyty jako liczbę zmiennoprzecinkową o maksymalnej precyzji 2.");
            return true;
        }
        return false;
    }

    public static boolean isPaidValidation(Model model, String value) {
        if(value == null || value.isEmpty()) {
            model.addAttribute("visitTimeError", "Podaj godzinę wizyty.");
            return true;
        }
        return false;
    }

    public static boolean visitStatusValidation(Model model, String value)
    {
        if(value == null || value.isEmpty()) {
            model.addAttribute("visitStatusError", "Podaj status wizyty.");
            return true;
        }
        if(value.length() > 30)
        {
            model.addAttribute("visitStatusError", "Status wizyty może zawierać co najwyżej 30 znaków.");
            return true;
        }
        if(!value.matches("^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*$"))
        {
            model.addAttribute("visitStatusError", "Status wizyty może składać się tylko z liter (pierwsza litera musi być wielka).");
            return true;
        }
        return false;
    }
}
