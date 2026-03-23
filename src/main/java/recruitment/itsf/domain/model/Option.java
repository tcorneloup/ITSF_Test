package recruitment.itsf.domain.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class Option {

    private Long id;

    @NotNull
    private OptionType optionType;

    private LocalDateTime optionSubDateStart;

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
