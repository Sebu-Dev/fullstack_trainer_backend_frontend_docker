import { create } from "zustand";
import questionsData from "../Question/data/questionData";
import type { Question, QuizSet } from "../Question/type/QuestionType";
import { FilterService } from "../services/FilterService";
import { LocalStorageService } from "../services/LocalStorageService";
import { QuizService } from "../services/QuizService";

interface QuizStore {
  questionList: Question[];
  quizSet: QuizSet;
  selectedCategories: string[];

  // Actions
  generateQuizSet: () => void;
  filterQuestions: (categories: string[]) => void;
  setSelectedCategories: (categories: string[]) => void;
  updateUserAnswer: (questionId: string, optionText: string) => void;
  resetQuizState: () => void;

  // Getters
  getQuestionPoints: (questionId: string) => number;
  getTotalPoints: () => number;
  getAllCategories: () => string[];
}

const useQuizStore = create<QuizStore>((set, get) => ({
  questionList: questionsData,
  quizSet: LocalStorageService.getQuizSet(),
  selectedCategories: [],

  /** Setzt den Quiz-Status zurück */
  resetQuizState: () => {
    const emptyQuizSet: QuizSet = {
      questions: [],
      answers: [],
      totalPossiblePoints: 0,
      totalAchievedPoints: 0,
    };
    LocalStorageService.saveQuizSet(emptyQuizSet);
    set({ quizSet: emptyQuizSet, selectedCategories: [] });
  },

  /** Erstellt ein neues Quiz-Set mit den aktuellen Kategorien */
  generateQuizSet: () => {
    const newQuizSet = QuizService.generateQuizSet(
      get().questionList,
      get().selectedCategories,
      6, // Anzahl der Fragen
    );
    LocalStorageService.saveQuizSet(newQuizSet);
    set({ quizSet: newQuizSet });
  },

  /** Aktualisiert die Antwort des Benutzers */
  updateUserAnswer: (questionId, optionText) => {
    const updatedQuizSet = QuizService.updateAnswer(
      get().quizSet,
      questionId,
      optionText,
    );
    LocalStorageService.saveQuizSet(updatedQuizSet);
    set({ quizSet: updatedQuizSet });
  },

  /** Filtert die Fragen basierend auf den ausgewählten Kategorien */
  filterQuestions: (categories) => {
    const filteredQuestions = FilterService.filterQuestions(
      get().questionList,
      categories,
    );
    set({
      quizSet: {
        ...get().quizSet,
        questions: filteredQuestions,
      },
    });
  },

  /** Setzt die ausgewählten Kategorien */
  setSelectedCategories: (categories) =>
    set({ selectedCategories: categories }),

  /** Gibt die Punkte für eine bestimmte Frage zurück */
  getQuestionPoints: (questionId) =>
    get().quizSet.answers.find((answer) => answer.question.id === questionId)
      ?.achievedPoints || 0,

  /** Gibt die Gesamtpunktzahl des Benutzers zurück */
  getTotalPoints: () => get().quizSet.totalAchievedPoints,

  /** Gibt alle verfügbaren Kategorien zurück */
  getAllCategories: () => FilterService.getAllCategories(get().questionList),
}));

export default useQuizStore;
