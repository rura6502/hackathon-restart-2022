package com.example.keywordalarmappbackend.keyword;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KeywordService {

  private final KeywordRepository keywordRepository;

  public List<Keyword> getKeywords(long memberId) {
    return keywordRepository.findAllByMemberId(memberId)
        .stream()
        .map(e -> new Keyword(e.getId(), e.getMemberId(), e.getTitle()))
        .collect(Collectors.toList());
  }

  @Transactional
  public Keyword registerKeyword(long memberId, RegisterKeywordCommand command) {
    KeywordEntity entity = KeywordEntity.create(memberId, command.getTitle());
    keywordRepository.save(entity);
    return new Keyword(entity.getId(), entity.getMemberId(), entity.getTitle());
  }

  @Transactional
  public void editKeyword(long keywordId, EditKeywordCommand command) {
    KeywordEntity entity = keywordRepository.findById(keywordId)
        .orElseThrow(() -> new RuntimeException("Keyword Not Found"));
    entity.editTitle(command.getTitle());
  }

  @Transactional
  public void deleteKeyword(long keywordId) {
    keywordRepository.deleteById(keywordId);
  }
}
