package com.web.bakery.service;

import com.web.bakery.model.FavoriteDTO;
import com.web.bakery.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FavoriteService {
    @Autowired
    FavoriteRepository favoriteRepository;

    public Page<FavoriteDTO> searchFav(Pageable pageable, UUID id , String search){
        return  favoriteRepository.getFavs(pageable,id, search);
    }

    public FavoriteDTO addToFav(FavoriteDTO favoriteDTO){
        if(favoriteRepository.getFavVIaIDs(favoriteDTO.getUser_id(),favoriteDTO.getAid_id()).isEmpty())
            return favoriteRepository.save(favoriteDTO);
        else {
            favoriteRepository.deleteFavVIaIDs(favoriteDTO.getUser_id(),favoriteDTO.getAid_id());
            return null;
        }
    }
    public Optional<FavoriteDTO> getFavViaIDs(UUID id, Integer aid){
        return favoriteRepository.getFavVIaIDs(id,aid);
    }
    public Integer getFavCount(UUID id, String search)
    {
        return favoriteRepository.getFavCount(id,search);
    }
}
