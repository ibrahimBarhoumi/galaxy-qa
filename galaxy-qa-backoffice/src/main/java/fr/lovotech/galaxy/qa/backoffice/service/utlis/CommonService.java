package fr.lovotech.galaxy.qa.backoffice.service.utlis;

import fr.lovotech.galaxy.qa.backoffice.config.UserAdditionalInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
    @Autowired
    private UserAdditionalInfoBean userAdditionalInfoBean;

    public String getCurrentOrganization() {
        return userAdditionalInfoBean.getCurrentOrganizationCode();
    }

    public  String getCurrentLanguage()  {
        return userAdditionalInfoBean.getCurrentLanguage();
    }


}
