package kpu.krosno.MediPets.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityData {
    private String cityId;
    private String postCode;
    private String city;
}
