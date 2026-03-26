package com.salon.service.impl;

import com.salon.domain.BookingStatus;
import com.salon.domain.SalonReport;
import com.salon.entity.Booking;
import com.salon.payload.dto.SalonDTO;
import com.salon.payload.dto.ServiceDTO;
import com.salon.payload.dto.UserDTO;
import com.salon.payload.request.BookingRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface IBookingService {
    Booking createBooking(BookingRequest booking, UserDTO user, SalonDTO salon, Set<ServiceDTO> serviceDTOs) throws Exception;
    List<Booking> getBookingByCustomer(Long customerId);
    List<Booking> getBookingBySalon(Long salonId);
    Booking updateBookingStatus(Long bookingId, BookingStatus status) throws Exception;
    List<Booking> getBookingByDate(LocalDate date, Long salonId);
    SalonReport getSalonReport(Long salonId);
    Booking getBookingById(Long id) throws Exception;

}
