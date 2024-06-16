package com.web.bakery.service;

import com.web.bakery.model.BakeryDTO;
import com.web.bakery.repository.BakeryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BakeryService {
    @Autowired
    private BakeryRepository aidRepository;

    public BakeryDTO createAid(BakeryDTO aid) {
        return aidRepository.save(aid);
    }

    public List<BakeryDTO> getAllAids() {
        return aidRepository.findAll();
    }
    public List<BakeryDTO> getSales() {
        return aidRepository.getSales();
    }
    public Page<BakeryDTO> getActiveAids(Pageable pageable) {
        return aidRepository.getActiveAids(pageable);
    }
    public Page<BakeryDTO> findAids(Pageable pageable, String search) {
        return aidRepository.searchAids(pageable, search);
    }

    public Page<BakeryDTO> searchViaPrice(Pageable pageable, String search, double min, double max){
        return  aidRepository.searchAidsWithPriceRangeOrderByPriceAsc(pageable,search,min,max);
    }

    public Optional<BakeryDTO> getAidById(Integer id) {
        return aidRepository.findById(id);
    }

    public void deleteAllAids() {
        aidRepository.deleteAll();
    }

    public boolean deleteAid(Integer id) {
        try {
            aidRepository.deleteSoftById(id);
        }catch (Exception ex){
            System.out.println("Request error: " + ex.getMessage());
            return false;
        }
        return true;
    }
    public boolean recoverAid(Integer id) {
        try {
            aidRepository.recoverAidById(id);
        }catch (Exception ex){
            System.out.println("Request error: " + ex.getMessage());
            return false;
        }
        return true;
    }
    public Integer getAidsCount()
    {
        return aidRepository.getAidsCount();
    }
    public Integer searchAidsCount(String search)
    {
        return aidRepository.getSearchAidsCount(search);
    }
    public Integer searchViaPriceCount(String search, double min, double max) {return aidRepository.countSearchAidsWithPriceRangeOrderByPriceAsc(search,min,max);}
}
