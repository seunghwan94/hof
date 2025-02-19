package com.lshwan.hof.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import com.lshwan.hof.domain.dto.PageRequestDto;
import com.lshwan.hof.domain.dto.PageResultDto;
import com.lshwan.hof.domain.dto.ProdDto;
import com.lshwan.hof.domain.dto.QnaDto;
import com.lshwan.hof.domain.dto.SearchRequestDto;
import com.lshwan.hof.domain.entity.admin.FWL;
import com.lshwan.hof.domain.entity.common.Qna;
import com.lshwan.hof.domain.entity.member.Member;
import com.lshwan.hof.domain.entity.prod.Prod;
import com.lshwan.hof.service.login.MemberService;
import com.lshwan.hof.service.admin.FwlService;
import com.lshwan.hof.service.common.QnaService;
import com.lshwan.hof.service.prod.ProdService;
import com.lshwan.hof.service.util.SearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;






@RestController
@AllArgsConstructor
@RequestMapping("admin/fwl")
@Tag(name = "FWL API", description = "금지어 관리 API")
public class FwlController {
  @Autowired
  private MemberService service;
  @Autowired
  private FwlService fwlService;
      @Autowired
    private SearchService searchService;
  @Autowired
  private ProdService prodService;

  @Autowired
  private QnaService qnaService;
    

  @GetMapping("/list")
  @Operation(summary = "회원 리스트 조회📝", description = "등록된 모든 회원을 조회합니다.")
  public List<Member> listtest() {
      return service.findList();
  }
  @PostMapping("/search")
    public List<Map<String, Object>> search(@RequestBody SearchRequestDto request) {
        return searchService.search(request);
    }
  @GetMapping
  @Operation(summary = "금지단어 리스트 조회🚫", description = "등록된 금지단어를 조회합니다.")
  public List<FWL> fwlList() {
    return fwlService.findList();
  }
  @PutMapping("/{fno}")
  @Operation(summary = "금지 단어 활성화 상태 수정", description = "등록된 금지단어를 활성화,비활성화 상태변환을 수정합니다.")
  public ResponseEntity<?> putMethodName(@PathVariable("fno") Long fno, @RequestBody FWL entity) {

    FWL existingFwl = fwlService.findBy(fno);

    existingFwl.setIsActive(entity.getIsActive());


    Long updatedId = fwlService.modify(existingFwl);

    return ResponseEntity.ok().body("수정 완료: " + updatedId);
  }
  @DeleteMapping("/{ids}")
  @Operation(summary = "금지 단어 삭제", description = "금지단어를 삭제합니다")
  public ResponseEntity<?> deleteFwl(@PathVariable("ids") String ids) {
      // "1,2,3" → [1, 2, 3] 변환
      List<Long> fnoList = Arrays.stream(ids.split(","))
                                .map(Long::parseLong)
                                .collect(Collectors.toList());

      if (fnoList.isEmpty()) {
          return ResponseEntity.badRequest().body("삭제할 항목을 선택하세요.");
      }

      int deleteCount = 0;
      for (Long fno : fnoList) {
          if (fwlService.remove(fno)) {
              System.out.println("삭제된 fno: " + fno);
              deleteCount++;
          }
      }
      return ResponseEntity.ok().body("삭제 완료: " + deleteCount + "개 항목");
  }

  
  @PostMapping
  @Operation(summary = "금지 단어 등록", description = "금지단어를 등록합니다")
  public ResponseEntity<?> addFwl(@RequestBody FWL entity) {
    Long newId = fwlService.add(entity);
     return ResponseEntity.ok().body("등록 완료: " + newId);
    // return ResponseEntity.badRequest().body(entity);
}
  
/////////////////////////prod
@GetMapping("/prod")
@Operation(summary = "상품 목록 페이징 🛍️", description = "등록된 상품을 조회합니다")
    public ResponseEntity<PageResultDto<ProdDto, Prod>> getProdList(@RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String keyword) {
        PageRequestDto dto = PageRequestDto.builder()
        .page(page)
        .size(size)
        .type(type)
        .keyword(keyword)
        .build();
        return ResponseEntity.ok().body(prodService.findList(dto));
    }
  ///////////////////////qna
@GetMapping("/qna")
@Operation(summary = "❓ 상품문의 리스트 조회 ", description = "등록된 모든 상품문의리스트를 조회합니다")
public ResponseEntity<List<QnaDto>> getQnaList() {
  List<QnaDto> dtoList = qnaService.findList();
  return ResponseEntity.ok(dtoList);
}
@PostMapping("/qna")
@Operation(summary = "상품 문의 답변 등록", description = "상품문의 답변을 등록합니다")
public ResponseEntity<?> registoryQna(@RequestBody QnaDto dto) {
  Member member = service.findBy(dto.getMemberId()); 
  if(member == null){
    member = Member.builder().id("hof").build();
  }

    Qna parentQna = (dto.getParentNo() != null) ? qnaService.findBy(dto.getParentNo()) : null;


    Long newId = qnaService.add(dto, member, parentQna);

    return ResponseEntity.ok("등록 완료: " + newId);
}
@PutMapping("/qna/{qno}")
public ResponseEntity<?> modifyQna(@PathVariable("qno") Long qno, @RequestBody Qna entity) {
    Qna existingQna = qnaService.findBy(qno);

    Long updatedId = qnaService.modify(existingQna);

    return ResponseEntity.ok().body("수정 완료: " + updatedId);
}
@DeleteMapping("/qna/{qno}")
@Operation(summary = "상품문의 삭제", description = "관리자가 상품문의 등록된걸 삭제합니다")
public ResponseEntity<?> deleteQna(@PathVariable("qno") Long qno){

  qnaService.remove(qno);
  return ResponseEntity.ok().body("삭제 완료" );
}

  
  
}
