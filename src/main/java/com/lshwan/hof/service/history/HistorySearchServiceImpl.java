package com.lshwan.hof.service.history;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lshwan.hof.domain.entity.history.mongo.HistorySearch;
import com.lshwan.hof.repository.mongo.HistorySearchRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class HistorySearchServiceImpl implements HistorySearchService{

  @Autowired
  private HistorySearchRepository historySearchRepository;
  @Override
  public String add(HistorySearch search) {
        return historySearchRepository.save(search).getId();
  }

  @Override
  public HistorySearch findBy(String id) {
    return historySearchRepository.findById(id).orElse(null);
  }

  @Override
  public List<HistorySearch> findList() {
    return historySearchRepository.findAll();
  }

  @Override
public String modify(HistorySearch search) {


    return historySearchRepository.save(search).getId();
}

  @Override
  public boolean remove(String id) {
    if (historySearchRepository.existsById(id)) {
      historySearchRepository.deleteById(id);
      return true;
    }
    return false;
  }
  
}
