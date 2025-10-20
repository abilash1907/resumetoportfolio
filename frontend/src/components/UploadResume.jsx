import { useState } from "react";
import { styles } from "../style";
import { extractContent } from "../services/services";
import { Button, CircularProgress } from "@mui/material";
export default function UploadResume({ code, setCode }) {//{ onData }
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleUpload = async () => {
    try {
      if (!file) return;
      setLoading(true);
      let res = await extractContent(file);
      console.log(res,"res")
      if(res){
        setCode({...code,content:res?.content});
      }else{
        setCode({...code,json:{}});
      }
    } catch (err) {
      console.log(err,"error");
      alert("Upload failed");
    } finally {
      setLoading(false);
    }
  };
  const handleFileUpload =(e) => {
    const uploadedFile = e.target.files[0];
    setFile(uploadedFile);
    // const reader = new FileReader();
    // reader.onload = (ev) => {
    //   setCode({ ...code, html: ev.target.result });
    // };
    // reader.readAsText(uploadedFile);
    // console.log(reader,"reader")
  };

  return (
    <section style={styles.uploadSection}>
      <h3 style={styles.uploadTitle}>Upload Option</h3>
      <input
        type="file"
        style={styles.fileInput}
        onChange={handleFileUpload}
      />
       <Button
        variant="contained"
        color="primary"
        onClick={handleUpload}
        style={styles.uploadBtn}
        endIcon={loading ? <CircularProgress size={20} color="inherit" /> : null}
        disabled={!file || loading}
      >
        {loading ? "Uploading..." : "Upload Resume"}
      </Button>
      {/* <button style={styles.uploadBtn}disabled={!file || loading} onClick={handleUpload}> {loading ? "Uploading..." : "Upload Resume"}</button> */}
      <span style={styles.fileName}>{file ? file.name : 'No file chosen'}</span>
    </section>
  );
}
