package org.dominokit.samples.attachments;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.modals.BaseModal;

import static java.util.Objects.nonNull;

public class AttachDialogComponent extends BaseModal<AttachDialogComponent> {

    public AttachDialogComponent(FileUploadComponent fileUploadComponent, CompleteHandler onCompleteHandler) {
        super("Attach files");
        init(this);
        setSize(ModalSize.LARGE);
        style.add("task-modal");
        appendChild(fileUploadComponent);
        this.appendFooterChild(Button.create(Icons.ALL.clear())
                .linkify()
                .setContent("CLOSE")
                .styler(style -> style.setMinWidth("120px"))
                .addClickListener(evt -> {
                    if (nonNull(onCompleteHandler)) {
                        onCompleteHandler.onComplete();
                    }
                    close();
                }));
    }

    public static AttachDialogComponent create(FileUploadComponent fileUploadComponent, CompleteHandler onCompletehandler) {
        return new AttachDialogComponent(fileUploadComponent, onCompletehandler);
    }

    @FunctionalInterface
    public interface CompleteHandler {
        void onComplete();
    }
}
