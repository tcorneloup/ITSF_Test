package Recruitement.ITSF.Bean;

import Recruitement.ITSF.Entity.OptionEntity;
import Recruitement.ITSF.Service.Enum.OptionType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OptionTest {

    @Test
    void fromOption_ShouldReturnNullIfOptionEntityIsNull() {
        assertNull(Option.fromOption(null));
    }

    @Test
    void fromOption_ShouldReturnOptionBeanIfOptionEntityIsNotNull() {

        LocalDateTime lDate = LocalDateTime.of(2026, 3, 10, 12, 30);

        OptionEntity lOptionEntity = new OptionEntity();
        lOptionEntity.setId(1L);
        lOptionEntity.setName(OptionType.NETFLIX);
        lOptionEntity.setOptionSubDateStart(lDate);

        Option lOption = Option.fromOption(lOptionEntity);

        assertNotNull(lOption);
        assertEquals(1L, lOption.getId());
        assertEquals(OptionType.NETFLIX, lOption.getName());
        assertEquals(lDate, lOption.getOptionSubDateStart());
    }

    @Test
    void toOptionEntity_ShouldReturnOptionEntity() {

        LocalDateTime lDate = LocalDateTime.of(2026, 3, 10, 12, 30);

        Option lOption = new Option();
        lOption.setId(1L);
        lOption.setName(OptionType.NETFLIX);
        lOption.setOptionSubDateStart(lDate);

        OptionEntity lOptionEntity = lOption.toOptionEntity();

        assertNotNull(lOptionEntity);
        assertEquals(1L, lOptionEntity.getId());
        assertEquals(OptionType.NETFLIX, lOptionEntity.getName());
        assertEquals(lDate, lOptionEntity.getOptionSubDateStart());
    }

}

