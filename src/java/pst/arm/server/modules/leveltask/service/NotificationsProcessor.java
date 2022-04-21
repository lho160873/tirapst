package pst.arm.server.modules.leveltask.service;

import com.extjs.gxt.ui.client.widget.MessageBox;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pst.arm.client.common.events.MessageEvent;
import pst.arm.client.modules.leveltask.domain.LevelTask;

import javax.annotation.processing.Completion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;

/**
 * @author wesStyle
 */

public class NotificationsProcessor implements Processor {

    @Autowired
    LevelTaskService service;

    public void process() {
        BroadcasterFactory bf = BroadcasterFactory.getDefault();
        Broadcaster broadcaster = bf.lookup("GWT_COMET");
        if (bf != null && service != null) {
            try {
                List<LevelTask> tasks = service.getUnsentLevelTasks();
                MessageEvent messageEvent;
                String msgInfo;
                for (LevelTask t : tasks) {
                    if (broadcaster != null) {
                        msgInfo = "Тема сообщения: " + t.getTaskName() + " от: " + t.getUserNameFrom();
                        MessageEvent.EMessageType type;
                        if (t.getTaskState() == 0) type = MessageEvent.EMessageType.MSG_IN;
                        else type = MessageEvent.EMessageType.MSG_OUT;
                        messageEvent = new MessageEvent(0, msgInfo, t.getUserIdFrom(), t.getUserIdTo(), type);
                        //System.out.println("Broadcasting from " + t.getUserIdFrom() + " to " + t.getUserIdTo());

                        broadcaster.broadcast(messageEvent);
//                        t.setSendSign(1);
//                        service.saveLevelTask(t, false);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Set<String> getSupportedOptions() {
        return null;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return null;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return null;
    }

    @Override
    public void init(ProcessingEnvironment processingEnvironment) {

    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
        return false;
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotationMirror, ExecutableElement executableElement, String s) {
        return null;
    }
}