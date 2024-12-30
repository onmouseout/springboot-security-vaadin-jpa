package io.firecontrol360.app;

import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;

@Route("todo") 
@AnonymousAllowed
public class TodoView extends VerticalLayout { 

    public TodoView(AuthenticationContext authContext) {
        var todosList = new VerticalLayout(); 
        var taskField = new TextField(); 
        var addButton = new Button("Add"); 
        addButton.addClickListener(click -> { 
            Checkbox checkbox = new Checkbox(taskField.getValue());
            todosList.add(checkbox);
        });
        addButton.addClickShortcut(Key.ENTER); 
        add( 
            new H1("Vaadin Todo"),
            todosList,
            new HorizontalLayout(taskField, addButton)
        );
        
        // Ensure that the class used by getAuthenticatedUser() matches the object type created
        // by the authentication providers used in Spring Security.
        authContext.getAuthenticatedUser(UserDetails.class).ifPresent(user -> {
            boolean isAdmin = user.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
            if (isAdmin) {
                add(new Button("Admin button"));
            } else {
                add(new H2("You are not an admin"));
            }
        });
    }
}