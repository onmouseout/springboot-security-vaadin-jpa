package io.firecontrol360.app;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

@Route("")
@PermitAll
public class DashboardView extends AppLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7255550026909100399L;
	private SecurityService securityService;

	public DashboardView(@Autowired SecurityService securityService) {
		this.securityService = securityService;

		
		// Set Header
		addToNavbar(createHeader());
		
		// Set Drawer (Side Menu)
		addToDrawer(createSideMenu());
		

		// Main Content (Placeholder for now)
		VerticalLayout content = new VerticalLayout(new Span("Willkommen im Dashboard!"));
		setContent(content);
	}

	private VerticalLayout createSideMenu() {
        VerticalLayout menuLayout = new VerticalLayout();
        menuLayout.addClassName("side-menu");

//        menuLayout.add(new Span("Menü"));
//        menuLayout.add(createMenuItem("Auftragsverwaltung"));
//        menuLayout.add(createMenuItem("Kundenliste"));
//        menuLayout.add(createMenuItem("Einstellungen"));

        SideNav nav = new SideNav();

        SideNavItem dashboardLink = new SideNavItem("Dashboard",
                DashboardView.class, VaadinIcon.DASHBOARD.create());
        SideNavItem inboxLink = new SideNavItem("Todo", "todo",VaadinIcon.ENVELOPE.create());
        
        SideNavItem vaadinLink = new SideNavItem("Vaadin website",
                "https://vaadin.com", VaadinIcon.VAADIN_H.create());

        nav.addItem(dashboardLink,inboxLink,vaadinLink);
        menuLayout.add(nav);
        
        return menuLayout;
    }

	private Span createMenuItem(String text) {
		Span menuItem = new Span(text);
		menuItem.addClassName("menu-item");
		menuItem.getElement().addEventListener("click", e -> {
			// Handle navigation here
			System.out.println("Navigating to: " + text);
		});
		return menuItem;
	}

	private HorizontalLayout createHeader() {
		HorizontalLayout headerLayout = new HorizontalLayout();
		headerLayout.addClassName("header");

		// Add logo or title
		Image logo = new Image("https://via.placeholder.com/100x50", "Logo");
		logo.addClassName("logo");

		// Add User Menu
		MenuBar userMenu = new MenuBar();
		userMenu.addItem("Profil", e -> System.out.println("Profil ausgewählt"));
		userMenu.addItem("Abmelden", e -> securityService.logout());
		
		DrawerToggle toggle = new DrawerToggle();
		headerLayout.add(toggle);

		headerLayout.add(logo, userMenu);
		return headerLayout;
	}
}