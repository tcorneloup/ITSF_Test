package recruitment.itsf.infra.entity;

import recruitment.itsf.domain.model.OptionType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "OPTIONS")
public class OptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "OPTION_TYPE", nullable = false)
    private OptionType optionType;

    @Column(name = "OPT_SUB_START_DATE", nullable = false)
    private LocalDateTime optionSubDateStart;

    public OptionEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }

    public LocalDateTime getOptionSubDateStart() {
        return optionSubDateStart;
    }

    public void setOptionSubDateStart(LocalDateTime optionSubDateStart) {
        this.optionSubDateStart = optionSubDateStart;
    }

}

