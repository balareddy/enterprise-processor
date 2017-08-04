package com.enterprise.message.service

import com.enterprise.message.Application
import com.enterprise.message.model.Message
import com.enterprise.message.repository.Repository
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Inject

import static com.enterprise.message.model.MessageType.*
import static com.enterprise.message.model.Priority.*

@ContextConfiguration(classes = [Application])
class ProcessorServiceSpec extends Specification {

    @Inject
    ProcessorService processorService

    @Inject
    Repository repository

    void setup() {
        repository.reset()
    }

    @Unroll
    def 'any message is received'() {
        when: 'message is received'
        def message = new Message(seqNum, msg, type, priority)
        processorService.offer(message)

        then: 'check the message store'
        def results = processorService.poll(message.type)
        results.size() == 1
        results[0] == message

        where: 'the messages are as below'
        seqNum | msg     | priority | type
        1L     | 'test1' | HIGH     | BLUE
        1L     | 'test1' | HIGH     | RED
        1L     | 'test1' | HIGH     | YELLOW
    }

    @Unroll
    def 'messages are received in the specified order'() {
        when: 'message is received'
        messages.each { message -> processorService.offer(message) }

        then: 'check the message store'
        messages.each { message ->
            def results = processorService.poll(message.type)
            def expectedResultByType = expectedResult.findAll { it.type == message.type }
            results.size() == expectedResultByType.size()
            results == expectedResultByType
        }

        where: 'the messages are as below'
        messages                                   | expectedResult
        // messages with same type, different priority
        [new Message(1L, 'test1', BLUE, LOW),
         new Message(2L, 'test2', BLUE, HIGH),
         new Message(3L, 'test3', BLUE, MEDIUM)]   | [new Message(2L, 'test2', BLUE, HIGH),
                                                      new Message(3L, 'test3', BLUE, MEDIUM),
                                                      new Message(1L, 'test1', BLUE, LOW)]
        // messages with different types and priority
        [new Message(1L, 'test1', BLUE, LOW),
         new Message(2L, 'test2', RED, HIGH),
         new Message(3L, 'test3', YELLOW, MEDIUM)] | [new Message(2L, 'test2', RED, HIGH),
                                                      new Message(3L, 'test3', YELLOW, MEDIUM),
                                                      new Message(1L, 'test1', BLUE, LOW)]
        // Messages are processed by Priority and FIFO order
        [new Message(1L, 'test1', BLUE, LOW),
         new Message(2L, 'test2', BLUE, LOW),
         new Message(3L, 'test3', BLUE, HIGH),
         new Message(4L, 'test4', BLUE, HIGH),
         new Message(5L, 'test5', BLUE, MEDIUM),
         new Message(6L, 'test6', BLUE, MEDIUM)]   | [new Message(3L, 'test3', BLUE, HIGH),
                                                      new Message(4L, 'test4', BLUE, HIGH),
                                                      new Message(5L, 'test5', BLUE, MEDIUM),
                                                      new Message(6L, 'test6', BLUE, MEDIUM),
                                                      new Message(1L, 'test1', BLUE, LOW),
                                                      new Message(2L, 'test2', BLUE, LOW)]
    }
}
