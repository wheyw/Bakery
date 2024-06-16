package com.web.bakery.service;

import com.web.bakery.model.HistoryItemDTO;
import com.web.bakery.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HistoryService {
    @Autowired
    HistoryRepository historyRepository;

    public List<HistoryItemDTO> getActive(UUID id)
    {
        return historyRepository.getActiveDeliveries(id);
    }
    public Page<HistoryItemDTO> getHistory(Pageable pageable, UUID id, String search){
        return  historyRepository.getHistory(pageable,id, search);
    }

    public HistoryItemDTO buyItem(HistoryItemDTO item){
        return historyRepository.save(item);
    }

    public Integer getHistorySize(UUID id, String search)
    {
        return historyRepository.getHistorySize(id, search);
    }

    public void confirmItem(UUID user,Integer id) { historyRepository.confirmItem(user,id);}
}
