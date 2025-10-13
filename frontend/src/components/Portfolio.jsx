export default function Portfolio({ data }) {
  if (!data) return null;

  return (
    <div className="p-8 mt-6 bg-white shadow rounded-xl w-full max-w-3xl">
      <h1 className="text-3xl font-bold">{data.name}</h1>
      <h2 className="text-xl text-blue-600 mb-4">{data.title}</h2>

      {data.skills?.length > 0 && (
        <section className="mt-4">
          <h3 className="font-semibold text-lg">Skills</h3>
          <ul className="list-disc pl-5 text-gray-700">
            {data.skills.map((skill, i) => (
              <li key={i}>{skill}</li>
            ))}
          </ul>
        </section>
      )}

      {data.experience?.length > 0 && (
        <section className="mt-4">
          <h3 className="font-semibold text-lg">Experience</h3>
          {data.experience.map((exp, i) => (
            <p key={i}>
              {exp.role} @ {exp.company} ({exp.years})
            </p>
          ))}
        </section>
      )}

      {data.projects?.length > 0 && (
        <section className="mt-4">
          <h3 className="font-semibold text-lg">Projects</h3>
          {data.projects.map((proj, i) => (
            <div key={i} className="mb-2">
              <p className="font-medium">{proj.title}</p>
              <p className="text-sm text-gray-600">{proj.description}</p>
            </div>
          ))}
        </section>
      )}
    </div>
  );
}
