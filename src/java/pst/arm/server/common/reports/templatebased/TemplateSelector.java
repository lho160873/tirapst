package pst.arm.server.common.reports.templatebased;

import java.io.InputStream;

/**
 *
 * @author Alexandr Kozhin
 * @since 0.0.1
 */
public interface TemplateSelector {
    public InputStream getTemplate();
}
