package com.project3.storemanagement.service;

import com.project3.storemanagement.entities.Variant;
import com.project3.storemanagement.entities.VariantsOrder;
import com.project3.storemanagement.dto.VariantDto;

import java.util.List;

public interface VariantService {

    List<Variant> listAllVariants();

    List<Variant> listAllVariantsByProductId(Long id);

    List<VariantsOrder> listVariantByOrderId(long id);

    Variant getVariantById(Long id);

    Variant updateVariant(long id, VariantDto variantDto);

    Variant deleteVariant(Long id);

    String getLastestVariantCode();
}
