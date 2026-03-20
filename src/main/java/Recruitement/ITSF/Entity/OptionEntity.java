package Recruitement.ITSF.Entity;

import Recruitement.ITSF.Service.Enum.OptionType;
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
    @Column(name = "NAME", nullable = false)
    private OptionType name;

    @Column(name = "OPT_SUB_START_DATE", nullable = false)
    private LocalDateTime optionSubDateStart;

    public OptionEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OptionType getName() {
        return name;
    }

    public void setName(OptionType name) {
        this.name = name;
    }

    public LocalDateTime getOptionSubDateStart() {
        return optionSubDateStart;
    }

    public void setOptionSubDateStart(LocalDateTime optionSubDateStart) {
        this.optionSubDateStart = optionSubDateStart;
    }

}

