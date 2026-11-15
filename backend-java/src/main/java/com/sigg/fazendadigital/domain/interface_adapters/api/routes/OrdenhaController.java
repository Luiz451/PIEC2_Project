package com.sigg.fazendadigital.domain.interface_adapters.api.routes;

import com.sigg.fazendadigital.domain.interface_adapters.api.dto.OrdenhaRequest;
import com.sigg.fazendadigital.domain.interface_adapters.api.dto.OrdenhaResponse;
import com.sigg.fazendadigital.domain.service.OrdenhaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ordenhas")
@RequiredArgsConstructor
public class OrdenhaController {

    private final OrdenhaService ordenhaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdenhaResponse cadastrar(@Valid @RequestBody OrdenhaRequest request) {
        return ordenhaService.cadastrar(request);
    }

    @GetMapping
    public Page<OrdenhaResponse> listar(
            @RequestParam UUID fazendaId,
            @RequestParam(required = false) UUID animalId,
            @RequestParam(required = false) LocalDate dataInicial,
            @RequestParam(required = false) LocalDate dataFinal,
            @PageableDefault(size = 20, sort = "dataOrdenha", direction = Sort.Direction.DESC) Pageable pageable) {
        if (pageable.getPageSize() > 100) {
            throw new IllegalArgumentException("O limite máximo é de 100 registros por página.");
        }
        return ordenhaService.listar(fazendaId, animalId, dataInicial, dataFinal, pageable);
    }

    @GetMapping("/{id}")
    public OrdenhaResponse buscar(@RequestParam UUID fazendaId, @PathVariable UUID id) {
        return ordenhaService.buscar(fazendaId, id);
    }

    @PutMapping("/{id}")
    public OrdenhaResponse atualizar(@RequestParam UUID fazendaId, @PathVariable UUID id,
                                     @Valid @RequestBody OrdenhaRequest request) {
        if (!fazendaId.equals(request.fazendaId())) {
            throw new IllegalArgumentException("A fazenda da rota deve corresponder à fazenda do registro.");
        }
        return ordenhaService.atualizar(fazendaId, id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@RequestParam UUID fazendaId, @PathVariable UUID id) {
        ordenhaService.remover(fazendaId, id);
    }
}
