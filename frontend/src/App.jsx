import { useState } from "react";
import UploadResume from "./components/UploadResume";
import Portfolio from "./components/Portfolio";

function App() {
  const [resume, setResume] = useState(null);

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col items-center p-8">
      <h1 className="text-2xl font-bold mb-6">
        Resume → Portfolio Converter
      </h1>

      <UploadResume onData={setResume} />
      <Portfolio data={resume} />
    </div>
  );
}

export default App;
