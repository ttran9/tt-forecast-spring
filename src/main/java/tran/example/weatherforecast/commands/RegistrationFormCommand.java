package tran.example.weatherforecast.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Used to bind (transfer) information from the registration form.
 */
@Setter
@Getter
@NoArgsConstructor
public class RegistrationFormCommand {

    // TODO: I will integrate a regex for more requirements on the password in a later release.

    @NotNull
    @Size(min = 4, max = 75)
    private String userName;

    @NotNull
    @Size(min=4, max=30)
    private String password;

    @NotNull
    @Size(min=4, max=30)
    private String verifyPassword;
}
