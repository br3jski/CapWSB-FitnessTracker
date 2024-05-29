package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.mail.api.EmailDto;
import com.capgemini.wsb.fitnesstracker.mail.api.EmailSender;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingService;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing {@link Training} entities.
 */
@Service

public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final UserProvider userProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public Training createTraining(Training training) {
        return trainingRepository.save(training);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Training> getTraining(Long trainingId) {
        return trainingRepository.findById(trainingId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Training> getTrainingsByUserId(Long userId) {
        return trainingRepository.findByUserId(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Training> getTrainingsEndedAfter(Date date) {
        return trainingRepository.findByEndTimeAfter(date);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Training> getTrainingsByActivityType(ActivityType activityType) {
        return trainingRepository.findByActivityType(activityType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Training updateTrainingDistance(Long trainingId, double distance) {
        return trainingRepository.findById(trainingId)
                .map(training -> {
                    training.setDistance(distance);
                    return trainingRepository.save(training);
                })
                .orElseThrow(() -> new TrainingNotFoundException(trainingId));
    }

    private final EmailSender emailSender;

    public TrainingServiceImpl(TrainingRepository trainingRepository, UserProvider userProvider, EmailSender emailSender) {
        this.trainingRepository = trainingRepository;
        this.userProvider = userProvider;
        this.emailSender = emailSender;
    }

    /**
     * Sends a training completion notification email to the user who completed the training.
     *
     * @param trainingId the ID of the completed training
     * @throws TrainingNotFoundException if the training with the specified ID is not found
     */
    @Override
    public void sendTrainingCompletionNotification(Long trainingId) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new TrainingNotFoundException(trainingId));

        User user = training.getUser();
        String recipientEmail = user.getEmail();

        String senderName = "CapWSB Fitness Tracker";
        String activityName = training.getActivityType().getDisplayName();
        long durationMinutes = (training.getEndTime().getTime() - training.getStartTime().getTime()) / (60 * 1000);

        String subject = "Trening ukończony";
        String content = String.format(
                " %s,\n\n" +
                        "Gratulacje! Kod nie wybuchł, a trening został zakończony.\n\n" +
                        "Szczegóły:\n" +
                        "- Aktywność: %s\n" +
                        "- Długośc treningu: %d minutes\n\n" +
                        "Tak trzymać!!\n\n" +
                        "%s",
                user.getFirstName(), activityName, durationMinutes, senderName);

        emailSender.send(new EmailDto(recipientEmail, subject, content));
    }


}