package it.polimi.telco_webapp.entities;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ServiceTypeConverter implements AttributeConverter<ServiceType, String>{

    @Override
    public String convertToDatabaseColumn(ServiceType serviceType) {
        String str = "";
        switch (serviceType) {
            case Mobile_Phone:
                str = "Mobile_Phone";
                break;
            /** Covered by default case:
             * case Fixed_Phone:
             * str = "Fixed_Phone";
             * break;
             */
            case Mobile_Internet:
                str = "Mobile_Internet";
                break;
            case Fixed_Internet:
                str = "Fixed_Internet";
                break;
            default:
                str = "Fixed_Phone";
        }
        return str;
    }

    @Override
    public ServiceType convertToEntityAttribute(String serviceTypeStr) {
        ServiceType serviceTypeEnum = null;
        switch (serviceTypeStr) {
            case "Mobile_Phone":
                serviceTypeEnum = ServiceType.Mobile_Phone;
                break;
            case "Fixed_Phone":
                serviceTypeEnum = ServiceType.Fixed_Phone;
                break;
            case "Mobile_Internet":
                serviceTypeEnum = ServiceType.Mobile_Internet;
                break;
            case "Fixed_Internet":
                serviceTypeEnum = ServiceType.Fixed_Internet;
                break;
        }
        return serviceTypeEnum;
    }
}
