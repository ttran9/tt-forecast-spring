package tran.example.weatherforecast.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tran.example.weatherforecast.commands.RegistrationFormCommand;
import tran.example.weatherforecast.domain.CustomUser;

/**
 * Converts the user entered user name and password into a CustomUser domain object.
 */
@Component
public class RegistrationFormCommandToCustomUser implements Converter<RegistrationFormCommand, CustomUser> {
    @Override
    public CustomUser convert(RegistrationFormCommand registrationFormCommand) {
        if(registrationFormCommand == null) {
            return null;
        }
        CustomUser user = new CustomUser();
        if(registrationFormCommand.getPassword() != null) {
            user.setPassword(registrationFormCommand.getPassword());
        }
        if(registrationFormCommand.getUserName() != null) {
            user.setUsername(registrationFormCommand.getUserName());
        }
        return user;
    }
}
