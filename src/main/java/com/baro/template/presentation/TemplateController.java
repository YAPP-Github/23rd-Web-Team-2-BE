package com.baro.template.presentation;

import com.baro.auth.domain.AuthMember;
import com.baro.template.application.TemplateService;
import com.baro.template.application.dto.ArchiveTemplateCommand;
import com.baro.template.application.dto.ArchiveTemplateResult;
import com.baro.template.application.dto.CopyTemplateCommand;
import com.baro.template.application.dto.FindTemplateQuery;
import com.baro.template.application.dto.FindTemplateResult;
import com.baro.template.application.dto.UnArchiveTemplateCommand;
import com.baro.template.domain.TemplateCategory;
import com.baro.template.presentation.dto.ArchiveTemplateRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RequestMapping("/templates")
@RestController
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping("/{category}")
    ResponseEntity<Slice<FindTemplateResult>> findTemplates(
            AuthMember authMember,
            @PathVariable("category") String categoryName,
            @RequestParam("sort") String sortName
    ) {
        Sort sort = SortType.getSort(sortName);
        FindTemplateQuery query = new FindTemplateQuery(TemplateCategory.getCategory(categoryName), sort);
        Slice<FindTemplateResult> result = templateService.findTemplates(query);

        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/{templateId}/copy")
    ResponseEntity<Void> copyTemplate(
            AuthMember authMember,
            @PathVariable("templateId") Long templateId
    ) {
        CopyTemplateCommand command = new CopyTemplateCommand(authMember.id(), templateId);
        templateService.copyTemplate(command);
        return ResponseEntity.noContent().build();
    }
}
