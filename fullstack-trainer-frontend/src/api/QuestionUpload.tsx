import { useState } from "react";

const QuestionUpload = () => {
  const [file, setFile] = useState<File | null>(null);
  const [message, setMessage] = useState<string | null>(null);

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files.length > 0) {
      setFile(event.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (!file) {
      setMessage("Bitte eine Datei auswählen.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch("http://localhost:8080/questions/upload", {
        method: "POST",
        body: formData,
      });
      const result = await response.text();
      setMessage(result);
    } catch (error) {
      setMessage("Fehler beim Hochladen.");
    }
  };

  return (
    <div className="p-4 bg-gray-100 rounded-lg shadow-lg w-96">
      <input type="file" accept="application/json" onChange={handleFileChange} className="mb-2" />
      <button onClick={handleUpload} className="bg-blue-500 text-white px-4 py-2 rounded">
        Hochladen
      </button>
      {message && <p className="mt-2 text-sm text-gray-700">{message}</p>}
    </div>
  );
};

export default QuestionUpload;
