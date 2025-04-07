package com.ohdelivery.service.match.matching.application;

import com.ohdelivery.service.match.matching.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.match.matching.application.dto.response.GetMatchingResponse;
import com.ohdelivery.service.match.matching.application.dto.request.UpdateMatchingRequest;
import com.ohdelivery.service.match.matching.domain.Matching;
import com.ohdelivery.service.match.matching.domain.repository.MatchingRepository;
import com.ohdelivery.service.match.matching.domain.vo.DeliveryInfo;
import com.ohdelivery.service.match.matching.domain.vo.PayInfo;
import com.ohdelivery.service.match.matching.domain.vo.RiderInfo;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService{
    private final MatchingRepository matchingRepository;

    @Override
    public UUID createMatching(CreateMatchingRequest request) {
        RiderInfo riderInfo = new RiderInfo(request.getRiderId());

        PayInfo payInfo = new PayInfo(request.getAssignedFee());

        DeliveryInfo deliveryInfo = new DeliveryInfo(
            request.getDeliveryId(),
            request.getStoreName(),
            request.getStoreAddress(),
            request.getDestinationAddress(),
            request.getDeliveryItem()
        );

        Matching matching = new Matching(riderInfo, payInfo, deliveryInfo);
        matchingRepository.save(matching);
        return matching.getId();
    }

    @Override
    public GetMatchingResponse getMatching(UUID id) {
        Matching matching = matchingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Matching not found"));

        return new GetMatchingResponse(
            matching.getId(),
            matching.getRiderInfo().getRiderId(),
            matching.getDeliveryInfo().getDeliveryId(),
            matching.getDeliveryInfo().getStoreName(),
            matching.getDeliveryInfo().getStoreAddress(),
            matching.getDeliveryInfo().getDestinationAddress(),
            matching.getDeliveryInfo().getDeliveryItem(),
            matching.getPayInfo().getAssignedFee()
        );
    }

    @Override
    public void updateMatching(UUID id, UpdateMatchingRequest request) {
        Matching matching = matchingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Matching not found"));

        matching = new Matching(
            new RiderInfo(request.getRiderId()),
            new PayInfo(request.getAssignedFee()),
            new DeliveryInfo(
                request.getDeliveryId(),
                request.getStoreName(),
                request.getStoreAddress(),
                request.getDestinationAddress(),
                request.getDeliveryItem()
            )
        );

        matchingRepository.save(matching);
    }

    @Override
    public void deleteMatching(UUID id) {
        matchingRepository.deleteById(id);
    }
}
