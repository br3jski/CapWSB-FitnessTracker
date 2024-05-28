package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingService;
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
@RequiredArgsConstructor
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
}