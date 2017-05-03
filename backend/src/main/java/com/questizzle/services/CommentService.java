package com.questizzle.services;

import com.questizzle.models.Comment;
import com.questizzle.exceptions.DataNotFoundException;
import com.questizzle.models.MiniUser;
import com.questizzle.models.User;
import com.questizzle.repositories.AssessmentRepository;
import com.questizzle.repositories.CommentRepository;
import com.questizzle.repositories.QuestionRepository;
import com.questizzle.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Danny on 3/29/2017.
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private UserService userService;

    public CommentService() {}

    public Comment getComment(String id) throws DataNotFoundException {
        Comment comment = commentRepository.findOne(id);

        if(comment == null) {
            throw new DataNotFoundException(id);
        }

        return comment;
    }

    public Comment createQuestionComment(String questionId, String text, String username) {
        User user = userRepository.findByUsername(username);

        Comment comment = new Comment();
        comment.setAuthor(new MiniUser(user.getIdentity(), username));
        comment.setQuestionId(questionId);
        comment.setText(text);
        comment.setPosted(new Date());

        Comment savedComment = commentRepository.save(comment);

        userService.incrementCount(username, "comments");

        return savedComment;
    }

    public Comment createAssessmentComment(String assessmentId, String text, String username) {
        User user = userRepository.findByUsername(username);

        Comment comment = new Comment();
        comment.setAuthor(new MiniUser(user.getIdentity(), username));
        comment.setAssessmentId(assessmentId);
        comment.setText(text);
        comment.setPosted(new Date());

        Comment savedComment = commentRepository.save(comment);

        userService.incrementCount(username, "comments");

        return savedComment;
    }

    public List<Comment> getQuestionComments(String questionId) throws DataNotFoundException {
        if(!questionRepository.exists(questionId)) {
            throw new DataNotFoundException(questionId);
        }

        List<Comment> comments = commentRepository.findByQuestionIdOrderByPostedDesc(questionId);

        return comments;
    }

    public List<Comment> getAssessmentComments(String assessmentId) throws DataNotFoundException {
        if(!assessmentRepository.exists(assessmentId)) {
            throw new DataNotFoundException(assessmentId);
        }

        List<Comment> comments = commentRepository.findByAssessmentIdOrderByPostedDesc(assessmentId);

        return comments;
    }
}
