package demo.unjuanable.application.orgmng.orgservice;

import demo.unjuanable.application.orgmng.orgservice.dto.CreateOrgRequest;
import demo.unjuanable.application.orgmng.orgservice.dto.OrgResponse;
import demo.unjuanable.application.orgmng.orgservice.dto.UpdateOrgBasicRequest;
import demo.unjuanable.common.framework.exception.BusinessException;
import demo.unjuanable.domain.orgmng.org.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrgService {
    private final OrgBuilderFactory orgBuilderFactory;
    private final OrgRepository orgRepository;
    private final OrgHandler orgHandler;

    @Autowired
    public OrgService(OrgBuilderFactory orgBuilderFactory
            , OrgHandler orgHandler
            , OrgRepository orgRepository) {
        this.orgBuilderFactory = orgBuilderFactory;
        this.orgHandler = orgHandler;
        this.orgRepository = orgRepository;
    }

    @Transactional
    public OrgResponse addOrg(CreateOrgRequest request, Long userId) {
        OrgBuilder builder = orgBuilderFactory.builder();

        Org org = builder.tenantId(request.getTenantId())
                .orgTypeCode(request.getOrgTypeCode())
                .leaderId(request.getLeaderId())
                .superiorId(request.getSuperiorId())
                .name(request.getName())
                .createdBy(userId)
                .build();

        org = orgRepository.save(org);

        return new OrgResponse(org);
    }

    @Transactional
    public OrgResponse updateOrgBasic(Long id, UpdateOrgBasicRequest request, Long userId) {
        Org org = orgRepository.findById(request.getTenantId(), id)
                .orElseThrow(() ->
                    new BusinessException("要修改的组织(id =" + id + "  )不存在！")
                );

        orgHandler.updateBasic(org, request.getName() , request.getLeaderId(), userId);
        orgRepository.save(org);

        return new OrgResponse(org);
    }

    @Transactional
    public Long cancelOrg(Long tenant, Long id, Long userId) {
        Org org = orgRepository.findById(tenant, id)
                .orElseThrow(() -> new BusinessException("要取消的组织(id =" + id + "  )不存在！")
                );

        orgHandler.cancel(org, userId);
        orgRepository.save(org);

        return org.getId();
    }

    public Optional<OrgResponse> findOrgById(Long tenantId, Long id) {
        return orgRepository.findById(tenantId, id).map(OrgResponse::new);
    }

}
