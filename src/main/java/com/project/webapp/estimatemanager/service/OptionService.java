package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.models.Opt;
import com.project.webapp.estimatemanager.repository.OptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

//TODO: inserire tutti i try catch
@Service
@Transactional
public class OptionService {
    private final OptionRepo optionRepo;

    @Autowired
    public OptionService(OptionRepo optionRepo) {
        this.optionRepo = optionRepo;
    }

    public Opt addOption(Opt option) {
        return optionRepo.save(option);
    }

    public List<Opt> findAllOptions() {
        return optionRepo.findAll();
    }

    public Opt updateOption(Opt option) {
        return optionRepo.save(option);
    }

    public Optional<Opt> findOptionById(Long id) {
        return optionRepo.findOptById(id);
    }

    public Optional<Opt> findOptionByName(String name) {
        return optionRepo.findOptByName(name);
    }

    public void deleteOption(Long id) {
        optionRepo.deleteOptById(id);
    }
}
