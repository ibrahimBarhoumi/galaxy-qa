package fr.lovotech.galaxy.qa.fileService.config;

import fr.lovotech.galaxy.qa.fileService.domain.ApplicationSource;
import fr.lovotech.galaxy.qa.fileService.service.GridFsTemplateSelector;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class GridFsTemplateInterceptor extends HandlerInterceptorAdapter {

    private final GridFsTemplateSelector gridFsTemplateSelector;
    private final GridFsTemplate defaultGridFsTemplate;

    public GridFsTemplateInterceptor(GridFsTemplateSelector gridFsTemplateSelector,
                                     @Qualifier(value = "defaultDataSource") GridFsTemplate defaultGridFsTemplate) {
        this.gridFsTemplateSelector = gridFsTemplateSelector;
        this.defaultGridFsTemplate = defaultGridFsTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(StringUtils.equals(request.getMethod(), "OPTIONS")){
            return true;
        }
        GridFsTemplate gridFsTemplate = defaultGridFsTemplate;
        gridFsTemplateSelector.setGridFsTemplate(gridFsTemplate);
        return true;
    }
}
