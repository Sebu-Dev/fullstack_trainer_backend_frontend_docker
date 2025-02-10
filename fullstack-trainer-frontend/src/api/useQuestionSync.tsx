import axios from "axios";
import { FaFile } from "react-icons/fa";
import { IconButton } from "sebu-dev-react-lib";
import useQuizStore from "../store/QuizStore";
const QuestionSyncButton = () => {
  const questionList = useQuizStore((state) => state.questionList);

  const handleSync = async () => {
    if (questionList.length === 0) {
      console.warn("Keine Fragen zum Synchronisieren.");
      return;
    }

    try {
      console.log(questionList);
      await axios.post("http://localhost:8080/questions/bulk");
      console.log("Fragen erfolgreich in die Datenbank geladen!");
    } catch (error) {
      console.error("Fehler beim Senden der Fragen:", error);
    }
  };

  return (
    <>
      <IconButton
        handleOnClick={handleSync}
        animationHover
        className="bg-transparent"
        size="text-2xl"
        icon={<FaFile />}
      ></IconButton>
    </>
  );
};

export default QuestionSyncButton;
