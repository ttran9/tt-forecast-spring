package tran.example.weatherforecast.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tran.example.weatherforecast.commands.RegistrationFormCommand;
import tran.example.weatherforecast.domain.User;

@Component
public class RegistrationFormCommandToUser implements Converter<RegistrationFormCommand, User> {
    @Override
    public User convert(RegistrationFormCommand registrationFormCommand) {
        if(registrationFormCommand == null) {
            return null;
        }
        User user = new User();
        if(registrationFormCommand.getPassword() != null) {
            user.setPassword(registrationFormCommand.getPassword());
        }
        if(registrationFormCommand.getUserName() != null) {
            user.setUsername(registrationFormCommand.getUserName());
        }
        return user;
    }
}
