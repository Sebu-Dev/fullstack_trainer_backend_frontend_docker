import axios from "axios";
import { FaFile } from "react-icons/fa";
import { IconButton } from "sebu-dev-react-lib";
import useQuizStore from "../store/QuizStore";

const QuestionSyncButton = () => {
  const questionList = useQuizStore((state) => state.questionList);

  const mapDifficultyToEnum = (difficulty: string | undefined): string | null => {
    switch (difficulty?.toLowerCase()) {
      case "easy":
        return "EASY";
      case "medium":
        return "MEDIUM";
      case "hard":
        return "HARD";
      default:
        return null;
    }
  };


  const scanDuplicates = () => {
    const textMap = new Map();

    questionList.forEach((question) => {
        const text = question.text;
        textMap.set(text, (textMap.get(text) || 0) + 1);
    });

    textMap.forEach((count, text) => {
        if (count > 1) {
            console.log(`Doppelter Eintrag: "${text}" (${count} Mal)`);
        }
    });
};

  const handleSync = async () => {
    scanDuplicates();
    if (questionList.length === 0) {
      console.warn("Keine Fragen zum Synchronisieren.");
      return;
    }

    const preparedQuestions = questionList.map(({ ...question }) => ({
      ...question,
      id:1,
      difficulty: mapDifficultyToEnum(question.difficulty) ?? null,
      explanation: question.explanation ?? null,
      imageUrl: question.imageUrl ?? null,
      maxPoints: question.maxPoints ?? null,
    }));

    try {
      await axios.post("http://localhost:8080/questions/bulk", preparedQuestions);
      console.log("Fragen erfolgreich in die Datenbank geladen!");
    } catch (error) {
      console.error("Fehler beim Senden der Fragen:", error);
    }
  };

  return (
    <IconButton
      handleOnClick={handleSync}
      animationHover
      className="bg-transparent"
      size="text-2xl"
      icon={<FaFile />}
    />
  );
};

export default QuestionSyncButton;
