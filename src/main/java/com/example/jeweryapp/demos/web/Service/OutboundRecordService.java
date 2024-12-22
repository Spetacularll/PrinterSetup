package com.example.jeweryapp.demos.web.Service;

import com.example.jeweryapp.demos.web.Entity.OutboundRecord;
import com.example.jeweryapp.demos.web.Repository.OutboundRecordRepository;
import com.example.jeweryapp.demos.web.common.OutRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboundRecordService {

    @Autowired
    private OutboundRecordRepository outboundRecordRepository;

    public List<OutRecord> getAllOutboundRecords() {
        return outboundRecordRepository.findAllOutboundRecords();
    }
}