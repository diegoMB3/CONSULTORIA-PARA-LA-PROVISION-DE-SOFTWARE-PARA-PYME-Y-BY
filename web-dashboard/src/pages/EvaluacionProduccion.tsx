import React, { useState } from 'react';

export const EvaluacionProduccion = () => {
  // Estados para clasificar los costos según RF-06.1
  const [ingresos, setIngresos] = useState<number>(0);
  const [costosVariables, setCostosVariables] = useState({ materiaPrima: 0, insumos: 0 });
  const [costosFijos, setCostosFijos] = useState({ alquiler: 0, sueldos: 0, servicios: 0 });
  const [loading, setLoading] = useState<boolean>(false);

  // Cálculos automáticos de eficiencia
  const totalCostosVariables = costosVariables.materiaPrima + costosVariables.insumos;
  const totalCostosFijos = costosFijos.alquiler + costosFijos.sueldos + serviciosTotales();
  
  function serviciosTotales() {
    return costosFijos.servicios;
  }
  
  const utilidadBruta = ingresos - totalCostosVariables;
  const utilidadNeta = utilidadBruta - totalCostosFijos;

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    const payload = {
      clienteId: "CLIENTE-DEMO-001", // Reemplazar dinámicamente con el ID real del flujo de navegación
      ingresos: ingresos,
      costosVariables: totalCostosVariables,
      costosFijos: totalCostosFijos,
      utilidadNeta: utilidadNeta
    };

    try {
      const response = await fetch('http://localhost:8080/api/evaluacion-produccion', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        throw new Error(`Error en el servidor: ${response.status}`);
      }

      const idGenerado = await response.text();
      alert(`¡Evaluación guardada exitosamente en PostgreSQL!\nID de Transacción: ${idGenerado}\nUtilidad Neta: Bs ${utilidadNeta}`);
    } catch (error) {
      console.error("Error al guardar la evaluación:", error);
      alert("Hubo un error al intentar conectarse con el servidor backend.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mx-auto p-6 max-w-4xl mt-8">
      <h1 className="text-3xl font-bold text-blue-900 mb-6 border-b-2 border-blue-900 pb-2">
        Evaluación de Producción (Industrial)
      </h1>
      
      <form onSubmit={handleSave} className="space-y-6">
        {/* 1. INGRESOS */}
        <div className="bg-white p-6 rounded-lg shadow-md border-t-4 border-blue-500">
          <h2 className="text-xl font-semibold mb-4 text-gray-800">1. Ingresos Mensuales Proyectados</h2>
          <div>
            <label className="block text-gray-700 font-medium">Ventas Totales (Bs)</label>
            <input type="number" className="w-full p-2 border border-gray-300 rounded mt-1 focus:ring-2 focus:ring-blue-500" 
                   value={ingresos} onChange={e => setIngresos(Number(e.target.value))} required disabled={loading} />
          </div>
        </div>

        {/* 2. COSTOS VARIABLES */}
        <div className="bg-white p-6 rounded-lg shadow-md border-t-4 border-yellow-500">
          <h2 className="text-xl font-semibold mb-4 text-gray-800">2. Costos Variables (Por Producción)</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-gray-700 font-medium">Materia Prima (Bs)</label>
              <input type="number" className="w-full p-2 border border-gray-300 rounded mt-1 focus:ring-2 focus:ring-yellow-500" 
                     value={costosVariables.materiaPrima} onChange={e => setCostosVariables({...costosVariables, materiaPrima: Number(e.target.value)})} required disabled={loading} />
            </div>
            <div>
              <label className="block text-gray-700 font-medium">Insumos (Bs)</label>
              <input type="number" className="w-full p-2 border border-gray-300 rounded mt-1 focus:ring-2 focus:ring-yellow-500" 
                     value={costosVariables.insumos} onChange={e => setCostosVariables({...costosVariables, insumos: Number(e.target.value)})} required disabled={loading} />
            </div>
          </div>
        </div>

        {/* 3. COSTOS FIJOS */}
        <div className="bg-white p-6 rounded-lg shadow-md border-t-4 border-red-500">
          <h2 className="text-xl font-semibold mb-4 text-gray-800">3. Costos Fijos (Operativos)</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="block text-gray-700 font-medium">Alquiler (Bs)</label>
              <input type="number" className="w-full p-2 border border-gray-300 rounded mt-1 focus:ring-2 focus:ring-red-500" 
                     value={costosFijos.alquiler} onChange={e => setCostosFijos({...costosFijos, alquiler: Number(e.target.value)})} required disabled={loading} />
            </div>
            <div>
              <label className="block text-gray-700 font-medium">Sueldos (Bs)</label>
              <input type="number" className="w-full p-2 border border-gray-300 rounded mt-1 focus:ring-2 focus:ring-red-500" 
                     value={costosFijos.sueldos} onChange={e => setCostosFijos({...costosFijos, sueldos: Number(e.target.value)})} required disabled={loading} />
            </div>
            <div>
              <label className="block text-gray-700 font-medium">Servicios (Bs)</label>
              <input type="number" className="w-full p-2 border border-gray-300 rounded mt-1 focus:ring-2 focus:ring-red-500" 
                     value={costosFijos.servicios} onChange={e => setCostosFijos({...costosFijos, servicios: Number(e.target.value)})} required disabled={loading} />
            </div>
          </div>
        </div>

        {/* 4. INDICADORES AUTOMÁTICOS */}
        <div className="bg-gray-100 p-6 rounded-lg shadow-md border border-gray-200">
          <h2 className="text-xl font-bold mb-4 text-center text-gray-800">Indicadores de Eficiencia</h2>
          <div className="flex flex-col md:flex-row justify-around text-lg">
            <p><strong>Total C. Variables:</strong> Bs {totalCostosVariables}</p>
            <p><strong>Total C. Fijos:</strong> Bs {totalCostosFijos}</p>
            <p className={`font-bold ${utilidadNeta >= 0 ? 'text-green-700' : 'text-red-600'}`}>
              <strong>Utilidad Neta:</strong> Bs {utilidadNeta}
            </p>
          </div>
        </div>

        <div className="text-right">
          <button 
            type="submit" 
            disabled={loading}
            className={`${loading ? 'bg-gray-500' : 'bg-blue-900 hover:bg-blue-800'} text-white px-8 py-3 rounded font-bold shadow-lg transition duration-200`}
          >
            {loading ? 'GUARDANDO...' : 'GUARDAR EVALUACIÓN'}
          </button>
        </div>
      </form>
    </div>
  );
};