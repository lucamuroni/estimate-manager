package com.project.webapp.estimatemanager.repository;

import com.project.webapp.estimatemanager.models.Opt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OptionRepoTest {
    @Autowired
    private OptionRepo optionRepo;

    private Opt opt;

    @BeforeEach
    public void init() {
        opt = Opt.builder().name("Sconto").type("2x1").build();
    }

    @Test
    public void OptionRepo_SaveAll_ReturnsSavedOption() {
        Opt savedOpt = optionRepo.save(opt);

        Assertions.assertThat(savedOpt).isNotNull();
        Assertions.assertThat(savedOpt.getId()).isGreaterThan(0);
    }

    @Test
    public void OptionRepo_GetAll_ReturnsAllOptionsFromDb() {
        Opt opt2 = Opt.builder().name("Sconto più grosso").type("2x1").build();
        optionRepo.save(opt);
        optionRepo.save(opt2);

        List<Opt> opts = optionRepo.findAll();

        Assertions.assertThat(opts).isNotNull();
        Assertions.assertThat(opts).isNotEmpty();
        Assertions.assertThat(opts.size()).isEqualTo(2);
    }

    @Test
    public void OptionRepo_FindById_ReturnsOptionWithThatId() {
        optionRepo.save(opt);

        Opt foundOpt = optionRepo.findOptById(opt.getId()).get();

        Assertions.assertThat(foundOpt).isNotNull();
    }

    @Test
    public void OptionRepo_FindByName_ReturnsOptionWithThatName() {
        optionRepo.save(opt);

        Opt foundOpt = optionRepo.findOptByName(opt.getName()).get();

        Assertions.assertThat(foundOpt).isNotNull();
    }

    @Test
    public void OptionRepo_UpdateOption_ReturnUpdatedOption() {
        Opt savedOpt = optionRepo.save(opt);

        savedOpt.setName("Sconticino");
        Opt updatedOpt = optionRepo.save(savedOpt);

        Assertions.assertThat(updatedOpt).isNotNull();
        Assertions.assertThat(updatedOpt.getName()).isEqualTo(savedOpt.getName());
    }

    @Test
    public void OptionRepo_DeleteOption_ReturnOpitonIsEmpty() {
        optionRepo.save(opt);

        optionRepo.deleteOptById(opt.getId());
        Optional<Opt> optReturn = optionRepo.findOptById(opt.getId());

        Assertions.assertThat(optReturn).isEmpty();
    }
}
