package com.salon.service;

import com.salon.domain.BookingStatus;
import com.salon.domain.SalonReport;
import com.salon.entity.Booking;
import com.salon.payload.dto.SalonDTO;
import com.salon.payload.dto.ServiceDTO;
import com.salon.payload.dto.UserDTO;
import com.salon.payload.request.BookingRequest;
import com.salon.repository.BookingRepository;
import com.salon.service.impl.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {

    private  final BookingRepository bookingRepository;

    @Override
    public Booking createBooking(BookingRequest booking, UserDTO user, SalonDTO salon, Set<ServiceDTO> serviceDTOs) throws Exception {
        int totalDuration = serviceDTOs.stream().mapToInt(ServiceDTO::getDuration).sum();

        LocalDateTime bookingStartTime = booking.getStartTime();
        LocalDateTime bookingEndTime = bookingStartTime.plusMinutes(60);

        Boolean isSlotAvailable = isTimeSlotAvailable(salon, bookingStartTime, bookingEndTime);
        int totalPrice = serviceDTOs.stream().mapToInt(ServiceDTO::getPrice).sum();
        Set<Long> idList = serviceDTOs.stream().map(ServiceDTO::getId).collect(Collectors.toSet());
        Booking newBooking = new Booking();
        newBooking.setCustomerId(user.getId());
        newBooking.setSalonId(salon.getId());
        newBooking.setServiceIds(idList);
        newBooking.setStatus(BookingStatus.PENDING);
        newBooking.setStartTime(bookingStartTime);
        newBooking.setEndTime(bookingEndTime);
        newBooking.setTotalPrice(totalPrice);


        return bookingRepository.save(newBooking);
    }

    public Boolean isTimeSlotAvailable(SalonDTO salonDTO, LocalDateTime bookingStartTime, LocalDateTime bookingEndTime) throws Exception {

        List<Booking> existingBookings=getBookingBySalon(salonDTO.getId());
        LocalDate bookingDate = bookingStartTime.toLocalDate();
        LocalDateTime salonOpenTime = salonDTO.getOpenTime().atDate(bookingDate);
        LocalDateTime salonCloseTime = salonDTO.getCloseTime().atDate(bookingDate);

        if (salonDTO.getCloseTime().isBefore(salonDTO.getOpenTime())) {
            salonCloseTime = salonCloseTime.plusMinutes(50);
        }



        for (Booking existingBooking : existingBookings){
            LocalDateTime existingBookingStartTime = existingBooking.getStartTime();
            LocalDateTime existingBookingEndTime = existingBooking.getEndTime();

            if (bookingStartTime.isBefore(existingBookingEndTime) && bookingEndTime.isAfter(existingBookingStartTime)){
                throw  new Exception("slot not available choose the different time");
            }

            if (bookingStartTime.isEqual(existingBookingStartTime) || bookingEndTime.isEqual(existingBookingEndTime)){
                throw new Exception("slot not available , choose different time");
            }

        }
        return true;
    }

    @Override
    public List<Booking> getBookingByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingBySalon(Long salonId) {
        return bookingRepository.findBySalonId(salonId);
    }

    @Override
    public Booking updateBookingStatus(Long bookingId, BookingStatus status) throws Exception {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingByDate(LocalDate date, Long salonId) {
        List<Booking> allBookings = getBookingBySalon(salonId);
        if (date==null){
            return allBookings;
        }
      return   allBookings.stream()
              .filter(booking -> isSameDate(booking.getStartTime(), date) ||
                      isSameDate(booking.getEndTime(), date))
              .collect(Collectors.toList());

    }

    private boolean isSameDate(LocalDateTime dateTime, LocalDate date) {
        return dateTime.toLocalDate().isEqual(date);
    }

    @Override
    public SalonReport getSalonReport(Long salonId) {
        List<Booking> bookings = getBookingBySalon(salonId);
        int totalEarning = bookings.stream().mapToInt(Booking::getTotalPrice).sum();
        Integer totalBooking = bookings.size();
        List<Booking> cancelledBooking = bookings
                .stream()
                .filter(booking -> booking.getStatus()
                        .equals(BookingStatus.CANCELLED))
                .collect(Collectors.toList());
        Double totalRefund = cancelledBooking.stream().mapToDouble(Booking::getTotalPrice).sum();
        SalonReport report = new SalonReport();
        report.setSalonId(salonId);
        report.setCancelledBooking(cancelledBooking.size());
        report.setTotalBookings(totalBooking);
        report.setTotalEarnings(totalEarning);
        report.setTotalRefund(totalRefund);
        return report;
    }

    @Override
    public Booking getBookingById(Long id) throws Exception {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking==null){
            throw new Exception("booking not found");
        }
        return booking;
    }
}
