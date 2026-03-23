package recruitment.itsf.domain.model;

import recruitment.itsf.infra.entity.OptionEntity;
import recruitment.itsf.infra.mapper.SubscriptionEntityMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OptionTest {

    @Test
    void optionEntityToDomain_ShouldReturnNullIfOptionEntityIsNull() {
        assertNull(SubscriptionEntityMapper.optionEntityToDomain(null));
    }

    @Test
    void optionEntityToDomain_ShouldReturnOptionWhenOptionEntityIsNotNull() {
        LocalDateTime lDate = LocalDateTime.of(2026, 3, 10, 12, 30);

        OptionEntity lOptionEntity = new OptionEntity();
        lOptionEntity.setId(1L);
        lOptionEntity.setOptionType(OptionType.NETFLIX);
        lOptionEntity.setOptionSubDateStart(lDate);

        Option lOption = SubscriptionEntityMapper.optionEntityToDomain(lOptionEntity);

        assertNotNull(lOption);
        assertEquals(1L, lOption.getId());
        assertEquals(OptionType.NETFLIX, lOption.getOptionType());
        assertEquals(lDate, lOption.getOptionSubDateStart());
    }

    @Test
    void optionDomainToEntity_ShouldReturnNullIfOptionIsNull() {
        assertNull(SubscriptionEntityMapper.optionDomainToEntity(null));
    }

    @Test
    void optionDomainToEntity_ShouldReturnOptionEntity() {
        LocalDateTime lDate = LocalDateTime.of(2026, 3, 10, 12, 30);

        Option lOption = new Option();
        lOption.setId(1L);
        lOption.setOptionType(OptionType.NETFLIX);
        lOption.setOptionSubDateStart(lDate);

        OptionEntity lOptionEntity = SubscriptionEntityMapper.optionDomainToEntity(lOption);

        assertNotNull(lOptionEntity);
        assertEquals(1L, lOptionEntity.getId());
        assertEquals(OptionType.NETFLIX, lOptionEntity.getOptionType());
        assertEquals(lDate, lOptionEntity.getOptionSubDateStart());
    }
}
