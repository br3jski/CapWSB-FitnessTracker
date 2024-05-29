package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.mail.api.EmailDto;
import com.capgemini.wsb.fitnesstracker.mail.api.EmailSender;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingServiceImplTest {

    private TrainingServiceImpl trainingService;
    private TrainingRepository trainingRepository;
    private UserProvider userProvider;
    private EmailSender emailSender;

    private Training training;
    private User user;

    @BeforeEach
    void setUp() {
        trainingRepository = new TrainingRepository() {
            private final Map<Long, Training> database = new HashMap<>();
            private long currentId = 1L;

            @Override
            public List<Training> findAll() {
                return new ArrayList<>(database.values());
            }

            @Override
            public List<Training> findByUserId(Long userId) {
                List<Training> result = new ArrayList<>();
                for (Training training : database.values()) {
                    if (training.getUser().getId().equals(userId)) {
                        result.add(training);
                    }
                }
                return result;
            }

            @Override
            public List<Training> findByEndTimeAfter(Date date) {
                List<Training> result = new ArrayList<>();
                for (Training training : database.values()) {
                    if (training.getEndTime().after(date)) {
                        result.add(training);
                    }
                }
                return result;
            }

            @Override
            public List<Training> findByActivityType(ActivityType activityType) {
                List<Training> result = new ArrayList<>();
                for (Training training : database.values()) {
                    if (training.getActivityType() == activityType) {
                        result.add(training);
                    }
                }
                return result;
            }

            @Override
            public Optional<Training> findById(Long id) {
                return Optional.ofNullable(database.get(id));
            }

            @Override
            public <S extends Training> S save(S entity) {
                if (entity.getId() == null) {
                    try {
                        Field idField = Training.class.getDeclaredField("id");
                        idField.setAccessible(true);
                        idField.set(entity, currentId++);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException("Failed to set id on entity", e);
                    }
                }
                database.put(entity.getId(), entity);
                return entity;
            }

            @Override
            public void deleteById(Long id) {
                database.remove(id);
            }

            @Override
            public void flush() {
                // No-op
            }

            @Override
            public <S extends Training> S saveAndFlush(S entity) {
                return save(entity);
            }

            @Override
            public <S extends Training> List<S> saveAllAndFlush(Iterable<S> entities) {
                List<S> result = new ArrayList<>();
                for (S entity : entities) {
                    result.add(save(entity));
                }
                return result;
            }

            @Override
            public void deleteAllInBatch(Iterable<Training> entities) {
                for (Training entity : entities) {
                    database.remove(entity.getId());
                }
            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Long> ids) {
                for (Long id : ids) {
                    database.remove(id);
                }
            }

            @Override
            public void deleteAllInBatch() {
                database.clear();
            }

            @Override
            public Training getOne(Long aLong) {
                return findById(aLong).orElse(null);
            }

            @Override
            public Training getById(Long aLong) {
                return findById(aLong).orElse(null);
            }

            @Override
            public Training getReferenceById(Long aLong) {
                return findById(aLong).orElse(null);
            }

            @Override
            public <S extends Training> List<S> saveAll(Iterable<S> entities) {
                List<S> result = new ArrayList<>();
                for (S entity : entities) {
                    result.add(save(entity));
                }
                return result;
            }

            @Override
            public long count() {
                return database.size();
            }

            @Override
            public void delete(Training entity) {
                database.remove(entity.getId());
            }

            @Override
            public void deleteAll() {
                database.clear();
            }

            @Override
            public void deleteAll(Iterable<? extends Training> entities) {
                for (Training entity : entities) {
                    database.remove(entity.getId());
                }
            }

            @Override
            public void deleteAllById(Iterable<? extends Long> ids) {
                for (Long id : ids) {
                    database.remove(id);
                }
            }

            @Override
            public boolean existsById(Long id) {
                return database.containsKey(id);
            }

            @Override
            public List<Training> findAllById(Iterable<Long> ids) {
                List<Training> result = new ArrayList<>();
                for (Long id : ids) {
                    findById(id).ifPresent(result::add);
                }
                return result;
            }

            @Override
            public <S extends Training> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends Training> List<S> findAll(Example<S> example) {
                return Collections.emptyList();
            }

            @Override
            public <S extends Training> List<S> findAll(Example<S> example, Sort sort) {
                return Collections.emptyList();
            }

            @Override
            public <S extends Training> Page<S> findAll(Example<S> example, Pageable pageable) {
                return Page.empty();
            }

            @Override
            public <S extends Training> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends Training> boolean exists(Example<S> example) {
                return false;
            }

            @Override
            public List<Training> findAll(Sort sort) {
                return findAll();
            }

            @Override
            public Page<Training> findAll(Pageable pageable) {
                List<Training> trainings = findAll();
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), trainings.size());
                return new PageImpl<>(trainings.subList(start, end), pageable, trainings.size());
            }

            @Override
            public <S extends Training, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
                return queryFunction.apply(new FluentQuery.FetchableFluentQuery<S>() {
                    @Override
                    public FetchableFluentQuery<S> sortBy(Sort sort) {
                        return this;
                    }

                    @Override
                    public <R1> FetchableFluentQuery<R1> as(Class<R1> resultType) {
                        return (FetchableFluentQuery<R1>) this;
                    }

                    @Override
                    public Page<S> page(Pageable pageable) {
                        return Page.empty();
                    }

                    @Override
                    public Stream<S> stream() {
                        return Stream.empty();
                    }

                    @Override
                    public long count() {
                        return 0;
                    }

                    @Override
                    public boolean exists() {
                        return false;
                    }

                    @Override
                    public List<S> all() {
                        return Collections.emptyList();
                    }

                    @Override
                    public S oneValue() {
                        return null;
                    }

                    @Override
                    public S firstValue() {
                        return null;
                    }

                    @Override
                    public FetchableFluentQuery<S> project(Collection<String> properties) {
                        return this;
                    }
                });
            }
        };

        userProvider = new UserProvider() {
            @Override
            public Optional<User> getUser(Long userId) {
                User user = new User();
                user.setId(userId);
                return Optional.of(user);
            }

            @Override
            public Optional<User> getUserByEmail(String email) {
                return Optional.empty();
            }

            @Override
            public List<User> findAllUsers() {
                return Collections.emptyList();
            }
        };

        emailSender = new EmailSender() {
            @Override
            public void send(EmailDto email) {
            }
        };

        trainingService = new TrainingServiceImpl(trainingRepository, userProvider, emailSender);

        user = new User();
        user.setId(1L);

        training = new Training(user, new Date(), new Date(), ActivityType.RUNNING, 10.0, 5.0);
        training.setId(1L);
        trainingRepository.save(training);


    }


    @Test
    void testCreateTraining() {
        Training newTraining = new Training(user, new Date(), new Date(), ActivityType.CYCLING, 15.0, 7.5);
        newTraining.setId(2L);

        Training createdTraining = trainingService.createTraining(newTraining);

        assertEquals(newTraining, createdTraining);
    }

    @Test
    void testGetTraining() {
        Optional<Training> foundTraining = trainingService.getTraining(1L);

        assertTrue(foundTraining.isPresent());
        assertEquals(training, foundTraining.get());
    }

    @Test
    void testGetAllTrainings() {
        List<Training> allTrainings = trainingService.getAllTrainings();

        assertEquals(1, allTrainings.size());
        assertEquals(training, allTrainings.get(0));
    }

    @Test
    void testGetTrainingsByUserId() {
        List<Training> trainingsByUser = trainingService.getTrainingsByUserId(1L);

        assertEquals(1, trainingsByUser.size());
        assertEquals(training, trainingsByUser.get(0));
    }

    @Test
    void testGetTrainingsEndedAfter() {
        Date date = new Date(System.currentTimeMillis() - 1000);
        List<Training> trainingsEndedAfter = trainingService.getTrainingsEndedAfter(date);

        assertEquals(1, trainingsEndedAfter.size());
        assertEquals(training, trainingsEndedAfter.get(0));
    }

    @Test
    void testGetTrainingsByActivityType() {
        List<Training> trainingsByActivityType = trainingService.getTrainingsByActivityType(ActivityType.RUNNING);

        assertEquals(1, trainingsByActivityType.size());
        assertEquals(training, trainingsByActivityType.get(0));
    }

    @Test
    void testUpdateTrainingDistance() {
        Training updatedTraining = trainingService.updateTrainingDistance(1L, 20.0);

        assertEquals(20.0, updatedTraining.getDistance());
    }

    @Test
    void testUpdateTrainingDistanceNotFound() {
        assertThrows(TrainingNotFoundException.class, () -> {
            trainingService.updateTrainingDistance(2L, 20.0);
        });
    }

    @Test
    void testSendTrainingCompletionNotification() {
        // Arrange
        Long trainingId = 1L;
        String recipientEmail = "tesciki.testowe@testujemy.test";
        String senderName = "Testującysię FitnessTracker";
        String activityName = "Running";
        long durationMinutes = 60;

        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setEmail(recipientEmail);

        Date startTime = new Date();
        Date endTime = new Date(startTime.getTime() + durationMinutes * 60 * 1000);

        Training training = new Training(user, startTime, endTime, ActivityType.RUNNING, 10.0, 5.0);
        training.setId(trainingId);

        trainingRepository.save(training);

        List<EmailDto> sentEmails = new ArrayList<>();

        EmailSender emailSender = email -> sentEmails.add(email);

        TrainingServiceImpl trainingService = new TrainingServiceImpl(trainingRepository, userProvider, emailSender);

        // Act
        trainingService.sendTrainingCompletionNotification(trainingId);

        // Assert
        assertEquals(1, sentEmails.size(), "Expected one email to be sent");

        EmailDto sentEmail = sentEmails.get(0);
        assertEquals(recipientEmail, sentEmail.toAddress(), "Unexpected recipient email");
        assertEquals("Trening ukończony", sentEmail.subject(), "Unexpected email subject");
        assertTrue(sentEmail.content().contains("John"), "Email content does not contain the expected user name");
        assertTrue(sentEmail.content().contains("Gratulacje! Kod nie wybuchł, a trening został zakończony."), "Email content does not contain the expected congratulations message");
        assertTrue(sentEmail.content().contains("Aktywność: Running"), "Email content does not contain the expected activity name");
        assertTrue(sentEmail.content().contains("Długośc treningu: 60 minutes"), "Email content does not contain the expected duration");
        assertTrue(sentEmail.content().contains("Tak trzymać!!"), "Email content does not contain the expected encouragement message");
        assertTrue(sentEmail.content().contains(senderName), "Email content does not contain the expected sender name");
    }
}