package com.example.supplychain.service;

import com.example.supplychain.exception.ResourceNotFoundException;
import com.example.supplychain.model.Supplier;
import com.example.supplychain.repository.SupplierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier updatedSupplier) {
        Supplier existing = getSupplierById(id);
        existing.setName(updatedSupplier.getName());
        existing.setContactPerson(updatedSupplier.getContactPerson());
        existing.setPhone(updatedSupplier.getPhone());
        existing.setEmail(updatedSupplier.getEmail());
        existing.setAddress(updatedSupplier.getAddress());
        return supplierRepository.save(existing);
    }

    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Supplier not found with ID: " + id);
        }
        supplierRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Transactional(readOnly = true)
    public Page<Supplier> getSuppliersPaged(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        if (search != null && !search.trim().isEmpty()) {
            return supplierRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
        }
        return supplierRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public long countSuppliers() {
        return supplierRepository.count();
    }
}
