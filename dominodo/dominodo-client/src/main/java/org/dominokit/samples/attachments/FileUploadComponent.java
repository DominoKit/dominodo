package org.dominokit.samples.attachments;

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.upload.FileUpload;
import org.dominokit.domino.ui.utils.BaseDominoElement;
import org.dominokit.samples.tasks.HasTask;

import static org.jboss.elemento.Elements.em;
import static org.jboss.elemento.Elements.h;


public class FileUploadComponent extends BaseDominoElement<HTMLDivElement, FileUploadComponent> {

    private final FileUpload fileUpload = FileUpload.create();

    public FileUploadComponent(HasTask hasTask) {
        fileUpload
                .multipleFiles()
                .setThumbSpans(4, 4, 4, 12, 12)
                .manualUpload()
                .accept("*/*")
                .appendChild(h(3).textContent("Drop files here or click to upload."))
                .appendChild(em().textContent("(This is just a demo upload. Selected files are not actually uploaded)"))
                .onAddFile(fileItem -> {
                    DomGlobal.console.info("adding attachement");
                    hasTask.getTask().getAttachments().add(fileItem.getFile().name);
                    fileItem.addRemoveHandler(file -> hasTask.getTask().getAttachments().remove(file.name));
                });
        init(this);
    }

    public static FileUploadComponent create(HasTask hasTask){
        return new FileUploadComponent(hasTask);
    }

    public FileUpload getFileUpload() {
        return fileUpload;
    }

    @Override
    public HTMLDivElement element() {
        return fileUpload.element();
    }
}
