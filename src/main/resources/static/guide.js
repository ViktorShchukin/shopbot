document.getElementById("poolForm").addEventListener("submit", async (e) => {
      e.preventDefault();

      const data = {
        poolType: document.getElementById("poolType").value,
        filterType: document.getElementById("filterType").value,
        poolDepth: Number(document.getElementById("poolDepth").value),
        poolLength: Number(document.getElementById("poolLength").value),
        poolWidth: Number(document.getElementById("poolWidth").value),
        poolDiameter: Number(document.getElementById("poolDiameter").value),
        poolVolume: 0,
      };

      try {
        const response = await fetch("http://localhost:8282/guide/pdf", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(data),
        });

        if (!response.ok) {
          throw new Error("Failed to generate PDF");
        }

        // 👇 THIS IS THE IMPORTANT PART
        const blob = await response.blob();

        // Option 1: Download PDF
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = "pool-guide.pdf";
        document.body.appendChild(a);
        a.click();
        a.remove();

        // Option 2 (alternative): Open in new tab
        // window.open(url);

      } catch (error) {
        console.error(error);
        alert("Error: " + error.message);
      }
});

const poolType = document.getElementById("poolType");
poolType.addEventListener("change", () => {
    document.getElementById("sh-rect").classList.toggle("hidden")
    document.getElementById("sh-cir").classList.toggle("hidden")
});