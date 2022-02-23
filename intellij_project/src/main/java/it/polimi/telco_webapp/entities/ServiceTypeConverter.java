package it.polimi.telco_webapp.entities;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ServiceTypeConverter implements AttributeConverter<ServiceType, String>{

    @Override
    public String convertToDatabaseColumn(ServiceType serviceType) {
        StringBuilder sb = new StringBuilder();
        switch (serviceType) {
            case Mobile_Phone:
                sb.append(serviceType.Mobile_Phone);
                break;
            case Fixed_Phone:
                sb.append(serviceType.Fixed_Phone);
                break;
            case Mobile_Internet:
                sb.append(serviceType.Mobile_Internet);
                break;
            case Fixed_Internet:
                sb.append(serviceType.Fixed_Internet);
                break;
        }
        return sb.toString();
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
