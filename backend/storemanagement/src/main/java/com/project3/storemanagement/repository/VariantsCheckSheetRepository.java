package com.project3.storemanagement.repository;

import com.project3.storemanagement.entities.VariantsCheckSheet;
import com.project3.storemanagement.entities.VariantsCheckSheetId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantsCheckSheetRepository extends JpaRepository<VariantsCheckSheet, VariantsCheckSheetId> {
}