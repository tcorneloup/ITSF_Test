package Recruitement.ITSF.Bean;

import Recruitement.ITSF.Entity.OptionEntity;
import Recruitement.ITSF.Service.Enum.OptionType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class Option {

    private Long id;

    @NotNull
    private OptionType name;

    private LocalDateTime optionSubDateStart;

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

    public static Option fromOption(OptionEntity pOptionEntity) {
        if (pOptionEntity == null) {
            return null;
        }

        Option lOption = new Option();
        lOption.setId(pOptionEntity.getId());
        lOption.setName(pOptionEntity.getName());
        lOption.setOptionSubDateStart(pOptionEntity.getOptionSubDateStart());

        return lOption;
    }

    public OptionEntity toOptionEntity() {
        OptionEntity lOptionEntity = new OptionEntity();
        lOptionEntity.setId(id);
        lOptionEntity.setName(name);
        lOptionEntity.setOptionSubDateStart(optionSubDateStart);

        return lOptionEntity;
    }
}
