package fr.lovotech.galaxy.qa.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TriggerVersion extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private ReferenceUID externalUid;
    private String name;
    private Float version;
    private String content;
    private String filePath;
    private Trigger trigger;
  //  private List<UITestOptionsLot> uiTestOptionsLots;
  //  private List<BatchOption> batchOptions;

}
