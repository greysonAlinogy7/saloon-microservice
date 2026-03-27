package com.salon.controller;

import com.salon.domain.BookingStatus;
import com.salon.domain.SalonReport;
import com.salon.entity.Booking;
import com.salon.mapper.BookingMapper;
import com.salon.mapper.BookingSlotDTO;
import com.salon.payload.dto.BookingDTO;
import com.salon.payload.dto.SalonDTO;
import com.salon.payload.dto.ServiceDTO;
import com.salon.payload.dto.UserDTO;
import com.salon.payload.request.BookingRequest;
import com.salon.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private  final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestParam Long salonId,
            @RequestBody BookingRequest bookingRequest) throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(salonId);
        salonDTO.setOpenTime(LocalTime.now());
        salonDTO.setCloseTime(LocalTime.now().plusHours(24));

        Set<ServiceDTO> serviceDTOSet = new HashSet<>();

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setPrice(3900);
        serviceDTO.setDuration(45);
        serviceDTO.setName("Hair cut for men");
        serviceDTO.setId(1L);

        serviceDTOSet.add(serviceDTO);
        Booking booking = bookingService.createBooking(
                bookingRequest,
                userDTO,
                salonDTO,
                serviceDTOSet
        );
        return  ResponseEntity.ok(booking);

    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getBookingByCustomer(){

        List<Booking> bookings = bookingService.getBookingByCustomer(1L);
        return ResponseEntity.ok(getBookingDTOs(bookings));

    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingBySalonId(@PathVariable Long bookingId) throws Exception {
        Booking bookings = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(BookingMapper.toDTO(bookings));

    }

    @GetMapping("/{bookingId}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(@PathVariable Long bookingId, @RequestParam BookingStatus status) throws Exception {
        Booking bookings = bookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(BookingMapper.toDTO(bookings));

    }

    @GetMapping("/slot/salon/{salonId}/date/{date}")
    public ResponseEntity<List<BookingSlotDTO>> getBookingByDate(@PathVariable Long salonId, @RequestParam LocalDate date) throws Exception {
       List <Booking> bookings = bookingService.getBookingByDate(date, salonId);
       List<BookingSlotDTO> slotDTOS=bookings.stream()
               .map(booking -> {
                   BookingSlotDTO slotDTO = new BookingSlotDTO();
                   slotDTO.setStartTime(booking.getStartTime());
                   slotDTO.setEndTime(booking.getEndTime());
                   return slotDTO;
       }).collect(Collectors.toList());
        return ResponseEntity.ok(slotDTOS);
    }

    @GetMapping("/report")
    public ResponseEntity<SalonReport> getSalonReport() throws Exception {
        SalonReport report = bookingService.getSalonReport(1L);
        return ResponseEntity.ok(report);
    }

    private  Set<BookingDTO> getBookingDTOs(List<Booking> bookings){
        return bookings.stream().map(booking -> {return
                BookingMapper.toDTO(booking);
        }).collect(Collectors.toSet());
    }
}
