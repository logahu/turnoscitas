package mx.org.infonavit.tools;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import org.apache.maven.model.Model;

//import org.springframework.ui.Model;
//import mx.org.infonavit.bean.LoginBean;


public class AuthorizationListener implements PhaseListener{

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        String currentPage = facesContext.getViewRoot().getViewId();
        boolean isLoginPage= (currentPage.lastIndexOf("index.xhtml") > -1) ? true : false;
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        Object usuario= session.getAttribute("usuario");
        
        
        if(!isLoginPage && usuario == null){
        	
            NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
            nh.handleNavigation(facesContext, null, "/index.xhtml");
            
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
    
}

