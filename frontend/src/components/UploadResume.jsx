import { useState } from "react";
import axios from "axios";

export default function UploadResume({ onData }) {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleUpload = async () => {
    if (!file) return;
    const formData = new FormData();
    formData.append("file", file);
    setLoading(true);
    try {
      const res = await axios.post("http://localhost:8080/api/upload", formData);
      onData(res.data);
    } catch (err) {
      console.log(err);
      alert("Upload failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col items-center gap-4 p-6 border rounded-xl shadow-md bg-white w-96">
      <input
        type="file"
        onChange={(e) => setFile(e.target.files[0])}
        className="p-2 border rounded w-full"
      />
      <button
        onClick={handleUpload}
        disabled={!file || loading}
        className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 w-full"
      >
        {loading ? "Uploading..." : "Upload Resume"}
      </button>
    </div>
  );
}
